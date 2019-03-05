
# report summary metrics, given run folder path 
# ref: http://illumina.github.io/interop/classillumina_1_1interop_1_1model_1_1summary_1_1stat__summary.html 

from interop import py_interop_run_metrics
from interop import py_interop_summary 
from interop import py_interop_run 
import numpy as np
import logging 
import sys 
import os  
import subprocess
from subprocess import Popen, PIPE 
from pprint import pprint
import datetime, time

import csv
from settings import MACHINES
from utils import * 

def parse_single(run_folder_path):
    run_folder = os.path.basename(run_folder_path) 
    run_metrics = py_interop_run_metrics.run_metrics() 
    summary = py_interop_summary.run_summary() 

    valid_to_load = py_interop_run.uchar_vector(py_interop_run.MetricCount, 0)
    py_interop_run_metrics.list_summary_metrics_to_load(valid_to_load)
    try: 
        run_metrics.read(run_folder_path, valid_to_load)
        py_interop_summary.summarize_run_metrics(run_metrics, summary)
        logging.info("Read {}, Lane {}".format(summary.size(), summary.lane_count()))
 
        summary_array = []
    
        for read_index in xrange(summary.size()): 
            for lane_index in xrange(summary.lane_count()): 
                read_summary = summary.at(read_index)
                lane_summary = read_summary.at(lane_index)
                summary_array.append([
                    run_folder,
                    parse_runid(run_folder), 
                    read_summary.read().number(), 
                    lane_summary.lane(),
                    parse_float(lane_summary.density().mean()/1000), parse_float(lane_summary.density().stddev()),
                    parse_float(lane_summary.percent_pf().mean()), parse_float(lane_summary.percent_pf().stddev()),
                    parse_float(lane_summary.reads()/1000000),
                    parse_float(lane_summary.reads_pf()/1000000), 
                    parse_float(lane_summary.percent_gt_q30()),
                    parse_float(lane_summary.percent_aligned().mean()), parse_float(lane_summary.percent_aligned().stddev()), 
                    parse_float(lane_summary.error_rate().mean()), parse_float(lane_summary.error_rate().stddev())
                ])
        with open(OUT_PATH, 'ab') as file: 
            writer = csv.writer(file)
            for s in summary_array: 
                writer.writerow(s) 
    except Exception, ex: 
        logging.warn("Skipping - ERROR: %s - %s"%(run_folder, str(ex)))

def main(runs): 
    logging.basicConfig(level = logging.INFO)
    for run_folder_path in runs: 
        run_folder = os.path.basename(run_folder_path)
        logging.info("Processing run folder: {}".format(run_folder))
        parse_single(run_folder_path)

def listdir_shell(dt_start, dt_end):
    files = [] 
    for machine in MACHINES: 
        path = os.path.join(BASE_PATH, machine)
        if os.path.isdir(path): 
            files += filterdir_shell(path, dt_start, dt_end)
        else: 
            logging.warn("path not found: {}".format(path))
    return files 

def filterdir_shell(path, dt_start, dt_end): 
    cmd = 'find '+ path + ' -mindepth 1 -maxdepth 1 -type d -newermt '+ dt_start +' ! -newermt ' + dt_end 
    process = subprocess.Popen(cmd.split(), stdout=subprocess.PIPE)
    return [v.rstrip('\n') for v in process.stdout.readlines()]

BASE_PATH = "/ifs/archive/GCL/hiseq/"
OUT_PATH="/home/upops/interop-feng/out-{}.csv".format(time.time())
dt_start = datetime.datetime(2018, 8, 1)
dt_end = datetime.datetime(2019, 2, 1)

if __name__ == '__main__': 
    files = listdir_shell(dt_start.strftime("%Y-%m-%d") , dt_end.strftime("%Y-%m-%d") )
    files.sort()
    main(files)
