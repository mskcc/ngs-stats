# report summary metrics, given run folder path
# ref: http://illumina.github.io/interop/classillumina_1_1interop_1_1model_1_1summary_1_1stat__summary.html

import logging
import os
import numpy as np
import requests
from flask import json
from settings import NOVASEQ, LIMSREST_BASE_URL, HISEQ_INPUT_PATH
from detect_new_run import *
from interop import py_interop_run, py_interop_table
from interop import py_interop_run_metrics
from interop import py_interop_summary
from requests.auth import HTTPBasicAuth
from utils import *
import pandas as pd

'''
The functions in this module extract the 'Run summary/Interop data' from the Illumina sequencing runs. The code connects to 
Illumina sequencing run folders on /ifs/input/GCL/hiseq/ and reads the Interop Data from all the subfolders. The Run summary data 
is then posted to LIMS database using ExemplarRest endpoint. This module uses Illumina interop libraries which can be found on
github at https://github.com/Illumina/interop 
'''


def parse_single_json(run_folder_path, xrange=None):
    run_folder = os.path.basename(run_folder_path)
    run_metrics = py_interop_run_metrics.run_metrics()
    summary = py_interop_summary.run_summary()
    valid_to_load = py_interop_run.uchar_vector(py_interop_run.MetricCount, 0)
    py_interop_run_metrics.list_summary_metrics_to_load(valid_to_load)
    percent_occupied_df = pd.DataFrame
    if 'MyRun' not in str(run_folder_path):
        percent_occupied_df = get_percent_occupied_by_lane(run_folder_path)
    summary_dict = {}
    try:
        run_metrics.read(run_folder_path, valid_to_load)
        py_interop_summary.summarize_run_metrics(run_metrics, summary)
        logging.info("Read: {}, Lane: {}".format(summary.size(), summary.lane_count()))
        for read_index in range(summary.size()):
            logging.info("Read {}".format(read_index + 1))
            summary_dict.setdefault("run", run_folder)
            for lane_index in range(summary.lane_count()):
                read_summary = summary.at(read_index)
                lane_summary = read_summary.at(lane_index)
                summary_dict.setdefault("data", []).append({
                    "runname": parse_runid(run_folder),
                    "read": read_summary.read().number(),
                    "lane": lane_summary.lane(),
                    "density": parse_float(lane_summary.density().mean() / 1000),
                    "density_stddev": parse_float(lane_summary.density().stddev() / 1000),
                    "clusterpf": parse_float(lane_summary.percent_pf().mean()),
                    "clusterpf_stddev": parse_float(lane_summary.percent_pf().stddev()),
                    "readsm": parse_float(lane_summary.reads() / 1000000),
                    "readspfm": parse_float(lane_summary.reads_pf() / 1000000),
                    "q30": parse_float(lane_summary.percent_gt_q30()),
                    "aligned": parse_float(lane_summary.percent_aligned().mean()),
                    "aligned_stddev": parse_float(lane_summary.percent_aligned().stddev()),
                    "errorrate": parse_float(lane_summary.error_rate().mean()),
                    "errorrate_stddev": parse_float(lane_summary.error_rate().stddev()),
                    "percent_occupied": parse_float(parse_lane_occupancy(lane_index, percent_occupied_df))
                })
        return summary_dict
    except Exception as ex:
        logging.warn("Skipping - ERROR: %s - %s" % (run_folder, str(ex)))


def parse_runs(runs):
    data = []
    for run_folder_path in runs:
        run_folder = os.path.basename(run_folder_path)
        logging.info("Processing run folder: {}".format(run_folder))
        json = parse_single_json(run_folder_path)
        if json:
            data.append(json)
    return data


def get_percent_occupied_by_lane(run_folder_path):
    df = pd.DataFrame
    for item in NOVASEQ:
        if 'myrun' not in run_folder_path.lower() and item.lower() in run_folder_path.lower():
            valid_to_load = py_interop_run.uchar_vector(py_interop_run.MetricCount, 0)
            valid_to_load[py_interop_run.ExtendedTile] = 1
            valid_to_load[py_interop_run.Tile] = 1
            valid_to_load[py_interop_run.Extraction] = 1
            run_metrics = py_interop_run_metrics.run_metrics()
            run_metrics.read(run_folder_path, valid_to_load)
            columns = py_interop_table.imaging_column_vector()
            py_interop_table.create_imaging_table_columns(run_metrics, columns)
            headers = get_headers(columns, run_folder_path)
            column_count = py_interop_table.count_table_columns(columns)
            row_offsets = py_interop_table.map_id_offset()
            py_interop_table.count_table_rows(run_metrics, row_offsets)
            data = np.zeros((row_offsets.size(), column_count), dtype=np.float32)
            py_interop_table.populate_imaging_table_data(run_metrics, columns, row_offsets, data.ravel())

            header_subset = ["Lane", "Tile", "Cycle", "% Occupied"]
            header_index = [(header, headers.index(header)) for header in header_subset]
            ids = np.asarray([headers.index(header) for header in header_subset[:3]])

            data_for_selected_header_subset = []
            for label, col in header_index:
                data_for_selected_header_subset.append(
                    (label, pd.Series([val for val in data[:, col]], index=[tuple(r) for r in data[:, ids]])))

            df = pd.DataFrame.from_dict(dict(data_for_selected_header_subset))
    return df 


def parse_lane_occupancy(lane_index, df):
    percent_occupied = 0.0
    if df.empty:
        return 0.0
    else:
        average_data = df.groupby('Lane').mean().reset_index()
        for index, row in average_data.iterrows():
            if row['Lane'] == lane_index + 1:
                percent_occupied = row['% Occupied']
                return percent_occupied


def get_headers(columns, run_folder_path):
    headers = []
    try:
        for i in range(columns.size()):
            column = columns[i]
            if column.has_children():
                headers.extend([column.name() + "(" + subname + ")" for subname in column.subcolumns()])
            else:
                headers.append(column.name())
    except Exception as ex: \
            logging.error("Error parsing data for lane occupancy metrics - ERROR: %s - %s" % (run_folder_path, str(ex)))

    return headers


def post_interopdata(data):
    with(open("Connect.txt")) as connectInfo:
        username = connectInfo.readline().strip()
        password = connectInfo.readline().strip()
        resp = requests.post(url=LIMSREST_BASE_URL + "addInteropData", data=json.dumps(data),
                             auth=HTTPBasicAuth(username, password), verify=False)
        logging.info(resp.text)


if __name__ == '__main__':
    logging.basicConfig(filename='logs/interop-{}.log'.format(datetime.datetime.now().strftime('%Y_%m_%d')),
                        level=logging.INFO, format="%(asctime)s.%(msecs)03d[%(levelname)-8s]: %(message)s ",
                        datefmt=" % Y - % m - % d % H: % M: % S")
    files = detect_new_runs()
    #files = detect_historical_runs((datetime.datetime.now() - datetime.timedelta(days=1*10)), datetime.datetime.now(), HISEQ_INPUT_PATH)
    files.sort()
    logging.info(files)
    logging.info(len(files))
    data = parse_runs(files)
    logging.info(data)
    if data:
        post_interopdata(data)
