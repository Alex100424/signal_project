package com.alerts.strategies;

import java.util.List;
import com.alerts.Alert;
import com.data_management.Patient;

/**
 * Strategy interface for checking patient data and generating alerts
 */
public interface AlertStrategy {
    
    /**
     * Checks patient data and adds generated alerts to the alert list
     * 
     * @param patient the patient whose data is checked
     * @param alertsthe list were generated alerts are stored
     */
    void checkAlert(Patient patient, List<Alert> alerts);
}
