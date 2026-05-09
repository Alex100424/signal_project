package com.alerts.factories;

import com.alerts.Alert;

/**
 * Factory for creating blood pressure alerts
 */
public class ManualAlertFactory extends AlertFactory {

    /**
     * Creates manual alert
     * 
     * @param patientID ID of the patient
     * @param condition the manual alert condition
     * @param timestamp time when alert happened
     * @return the created manual alert
     */
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        return new Alert(patientID, condition, timestamp);
    }
}
