delete from HOSPITAL;
delete from MEDICALDSR;
delete from MEDICALDSRTYPE;
delete from MEDICALDSRSTOCKMOVTYPE;
delete from DELIVERYTYPE;
delete from DELIVERYRESULTTYPE;
delete from PREGNANTTREATMENTTYPE;
delete from EXAMROW;
delete from EXAM;
delete from EXAMTYPE;
delete from OPERATION;
delete from OPERATIONTYPE;
delete from DISEASE;
delete from DISEASETYPE;
delete from VACCINE;
delete from ADMISSIONTYPE;
delete from DISCHARGETYPE;
delete from WARD;

-- HOSPITAL
INSERT INTO HOSPITAL (HOS_ID_A,HOS_NAME,HOS_ADDR,HOS_CITY,HOS_TELE,HOS_FAX,HOS_EMAIL,HOS_LOCK) VALUES 
 ('STLUKE','St. Luke HOSPITAL - Angal','P.O. BOX 85 - NEBBI','ANGAL','+256 0472621076','+256 0','angal@ucmb.ug.co.',0);

-- MEDICALDSRTYPE
LOAD DATA LOCAL INFILE './data_ar/medicaldsrtype.csv'
	INTO TABLE MEDICALDSRTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- MEDICALDSRSTOCKMOVTYPE
LOAD DATA LOCAL INFILE './data_ar/medicaldsrstockmovtype.csv'
	INTO TABLE MEDICALDSRSTOCKMOVTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- DELIVERYTYPE
LOAD DATA LOCAL INFILE './data_ar/deliverytype.csv'
	INTO TABLE DELIVERYTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- DELIVERYRESULTTYPE
LOAD DATA LOCAL INFILE './data_ar/deliveryresulttype.csv'
	INTO TABLE DELIVERYRESULTTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- PREGNANTTREATMENTTYPE
LOAD DATA LOCAL INFILE './data_ar/pregnanttreatmenttype.csv'
	INTO TABLE PREGNANTTREATMENTTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- EXAMTYPE
LOAD DATA LOCAL INFILE './data_ar/examtype.csv'
	INTO TABLE EXAMTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- EXAM
LOAD DATA LOCAL INFILE './data_ar/exam.csv'
	INTO TABLE EXAM 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- EXAMROW
LOAD DATA LOCAL INFILE './data_ar/examrow.csv'
	INTO TABLE EXAMROW 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- OPERATIONTYPE
LOAD DATA LOCAL INFILE './data_ar/operationtype.csv'
	INTO TABLE OPERATIONTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- VACCINE
LOAD DATA LOCAL INFILE './data_ar/vaccine.csv'
	INTO TABLE VACCINE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- ADMISSIONTYPE
LOAD DATA LOCAL INFILE './data_ar/admissiontype.csv'
	INTO TABLE ADMISSIONTYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- DISCHARGETYPE
LOAD DATA LOCAL INFILE './data_ar/dischargetype.csv'
	INTO TABLE DISCHARGETYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- DISEASETYPE
LOAD DATA LOCAL INFILE './data_ar/diseasetype.csv'
	INTO TABLE DISEASETYPE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- DISEASE
LOAD DATA LOCAL INFILE './data_ar/disease.csv'
	INTO TABLE DISEASE 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- OPERATION
LOAD DATA LOCAL INFILE './data_ar/operation.csv'
	INTO TABLE OPERATION 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- MEDICALDSR
LOAD DATA LOCAL INFILE './data_ar/medicaldsr.csv'
	INTO TABLE MEDICALDSR 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';

-- WARD
LOAD DATA LOCAL INFILE './data_ar/ward.csv'
	INTO TABLE WARD 
	FIELDS TERMINATED BY ';' 
	LINES TERMINATED BY '\n';
