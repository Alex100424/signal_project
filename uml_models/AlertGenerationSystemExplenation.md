# Alert Generation System
This diagram models the alert generation of the CHMS. Its purpose is to generate alerts when the data of a patient comes back with a 
measurment outside an acceptable threshold. The main class in my design is 'AlertGenerator' and its responsability is coordinating
the process. It recieves patient data and it uses 'ConditionEvaluator' to check weather the data violates any rules. The 
'ConditionEvaluator' depends on a list of 'Threshold' objects. This makes the design better because it allows the user to change 
the threshold values without having to rewrite the whole class. If a threshold is surpassed, 'AlertGenerator' creates an 'Alert'
object that contains the alertId, patientId, condition and timestamp. 'AlertManager' is responsible for managing and sending out the 
alerts after they are generated. This way, alert generaition is kept separatelly from alert delivery. Overall, this design makes it 
easy for the user to make changes to the code later on without having to change the whole logic of the code. The separation of the
responsabilities improves readability and maintenance, because each class a clear task. This design could be improved in the future
by adding more advanced evaluation rules, diffrent alert types or more ways of how hospital staff should be notified.