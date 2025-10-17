USE hospital;

SET GLOBAL sql_mode = '';
SET SESSION sql_mode = '';

LOAD DATA INFILE 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\patients.csv'
INTO TABLE patient_info
CHARACTER SET gbk
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;
##载入SCV表