# Data Storage System

The diagram models the data storage subsystem of the CHMS. Its purpose is to store incoming patient data safely and make it 
accessible later on for monitoring and analysis. In my diagram, 'DataStorage' is the main class, it is responsible for storing 
'PatientData', retrieving records for a specific patient and deleting records that are no longer usefull. Each 'PatientData' object
represents one measurment and contains the patientID, the timestamp, the vital type and value. This completes the requirments 
that the data should be timestamped and retrievable for each patient. The design also includes the class 'DataRetriever', which 
handles requests for patient data and the needed records for that case from 'DataStorage'. 'MedicalStaff' requests data through 
'DataRetriever' instead of directly accessing 'DataStorage'. This makes the system easier to maintain. 'AccessControl' is used to 
check whether a member of the hospital staff is allowed to access a patient's records. 'Expiration' is responsible for checking if 
the stored data is too old and should be deleted. 'Logs' records access and deletion actions. This way, the system keeps track of
who used the data and what changes happened. Overall, the design separates storage, retrieval, access checking, delition and logs
separately, which makes the system easier to extend in the future.