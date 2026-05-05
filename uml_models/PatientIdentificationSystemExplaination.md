# Patient Identification System

This system ensures that each incoming measurement is linked to the correct hospital patient. IdentityManager is the controller: it  
receives incoming data, asks PatientIdentifier to verify and match the patient ID, and handles errors such as invalid IDs, no match 
or suspicious anomalies. PatientIdentifier performs the actual lookup logic, using hospital records to find the correct patient. 
HospitalPatient represents the verified patient and contains personal information and medical history. PatientRecord stores 
individual measurements linked to that patient. Together, this system protects data integrity by making sure medical data is 
assigned to the right person before the rest of the CHMS uses it.