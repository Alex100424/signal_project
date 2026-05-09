package com.alerts.factories;

import com.alerts.Alert;

/**
 * Factory for creating blood pressure alerts
 */
public class BloodPressureAlertFactory extends AlertFactory {

    /**
     * Creates blood pressure alert
     * 
     * @param patientID ID of the patient
     * @param condition the blood pressure condition
     * @param timestamp time when alert happened
     * @return the created blood pressure alert
     */
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        return new Alert(patientID, condition, timestamp);
    }
}
