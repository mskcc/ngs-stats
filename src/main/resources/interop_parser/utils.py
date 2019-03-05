import re
import math

def parse_runid(run_folder): 
    ma = re.match("^[0-9]{6}_([0-9A-Z]+_[0-9]{4}_.*)", run_folder)
    if ma: 
        return ma.groups()[0]

def parse_float(val): 
    if math.isnan(val): 
        return 0
    return round(val, 2)

