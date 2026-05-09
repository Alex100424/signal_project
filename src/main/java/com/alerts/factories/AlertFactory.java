package com.alerts.factories;

import com.alerts.Alert;

/**
 * Base factory for creating alerts
 */
public abstract class AlertFactory {

    /**
     * Creates alert for a patient
     * 
     * @param patiendID ID of the patient
     * @param condition condition that caused the alert
     * @param timestamp time when alert happened
     * @return the created alert
     */
    public abstract Alert createAlert(String patiendID, String condition, long timestamp);
}
