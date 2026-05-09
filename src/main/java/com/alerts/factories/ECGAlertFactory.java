package com.alerts.factories;

import com.alerts.Alert;

/**
 * Factory for creating ECG alerts
 */
public class ECGAlertFactory extends AlertFactory {

    /**
     * Creates ECG alert
     * 
     * @param patientID ID of the patient
     * @param condition the ECG condition
     * @param timestamp time when alert happened
     * @return the created ECG alert
     */
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        return new Alert(patientID, condition, timestamp);
    }
}
