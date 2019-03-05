import mysql.connector
from datetime import datetime
import csv
from settings import DB_CONFIG

# set up interop table schema
# insert records into interop table

def create_table(config): 
    stmt_create = """
    CREATE TABLE interop (
        run_folder VARCHAR(255) NOT NULL,
        run_id VARCHAR(255) NOT NULL,
        read_number INT UNSIGNED NOT NULL, 
        lane INT UNSIGNED NOT NULL, 
        density FLOAT DEFAULT 0, 
        density_stddev FLOAT DEFAULT 0, 
        percent_pf FLOAT DEFAULT 0, 
        percent_pf_stddev FLOAT DEFAULT 0, 
        reads_m FLOAT DEFAULT 0, 
        reads_pf FLOAT DEFAULT 0, 
        percent_gt_q30 FLOAT DEFAULT 0, 
        percent_aligned FLOAT DEFAULT 0, 
        percent_aligned_stddev FLOAT DEFAULT 0, 
        error_rate FLOAT DEFAULT 0, 
        error_rate_stddev FLOAT DEFAULT 0, 
        date_entered TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (run_folder, read_number, lane)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
    """
    db = mysql.connector.connect(**config)
    cursor = db.cursor()
    print "Connection ID: %s" % db.connection_id 
    cursor.execute(stmt_create) 
    cursor.close()
    db.close()

def query(config): 
    db = mysql.connector.connect(**config)
    cursor = db.cursor()
    print "Connection ID: %s" % db.connection_id 

    cursor.execute('select * from picardfile limit 5')
    for row in cursor: 
        print row 

    cursor.close()
    db.close()

def insert(config, file_path): 
    csv_data = csv.reader(file(file_path))
    sql = "insert into interop (run_folder, run_id, read_number, lane, density, density_stddev, percent_pf, percent_pf_stddev, reads_m, reads_pf, percent_gt_q30, percent_aligned, percent_aligned_stddev, error_rate, error_rate_stddev) value(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)" 
    data = [] 
    for row in csv_data: 
        data.append(tuple(row))
    
    print "{} rows to be inserted".format(len(data)) 

    db = mysql.connector.connect(**config)
    cursor = db.cursor()
    print "Connection ID: %s" % db.connection_id 
    cursor.executemany(sql, data)
    db.commit()
    
    print "{} records inserted".format(cursor.rowcount) 

    cursor.close()
    db.close() 

if __name__ == '__main__':
    file_path = 'out-1551217686.18.csv'
    insert(DB_CONFIG, file_path)
