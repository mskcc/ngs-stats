
# report summary metrics, given run folder path 
# ref: http://illumina.github.io/interop/classillumina_1_1interop_1_1model_1_1summary_1_1stat__summary.html 

import logging 
import sys 
import os  
import subprocess
from subprocess import Popen, PIPE 
import datetime, time

from settings import MACHINES 
from utils import * 

def detect_historical_runs(dt_start, dt_end, hiseq_base_path):
    dt_start = dt_start.strftime("%Y-%m-%d")
    dt_end = dt_end.strftime("%Y-%m-%d")
    files = []
    for machine in MACHINES: 
        path = os.path.join(hiseq_base_path, machine)
        if os.path.isdir(path): 
            files += filterdir_shell(path, dt_start, dt_end)
        else: 
            logging.warn("path not found: {}".format(path))
    return files 

def filterdir_shell(path, dt_start, dt_end): 
    cmd = 'find '+ path + ' -mindepth 1 -maxdepth 1 -type d -newermt '+ dt_start +' ! -newermt ' + dt_end 
    process = subprocess.Popen(cmd.split(), stdout=subprocess.PIPE)
    return [str(v.rstrip(b'\n'), 'utf-8') for v in process.stdout.readlines()]

def detect_new_runs(): 
    session = subprocess.Popen(['sh', 'DetectNewRuns.sh'], stdout=PIPE, stderr=PIPE)
    stdout, stderr = session.communicate()
    return stdout.splitlines()
