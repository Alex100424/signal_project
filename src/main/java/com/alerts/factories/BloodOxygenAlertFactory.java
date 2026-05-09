package com.alerts.factories;

import com.alerts.Alert;

/**
 * Factory for creating blood oxygen alerts
 */
public class BloodOxygenAlertFactory extends AlertFactory {

    /**
     * Creates blood oxygen alert
     * 
     * @param patientID ID of the patient
     * @param condition the oxygen saturation condition
     * @param timestamp time when alert happened
     * @return the created blood oxygen alert
     */
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        return new Alert(patientID, condition, timestamp);
    }
}
