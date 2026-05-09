package com.alerts.strategies;

import java.util.*;
import com.alerts.Alert;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.data_management.*;

/**
 * Strategy for checking oxygen saturation alerts
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    private BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();

    /**
     * Checks oxygen saturation records for low values and rapid drops
     *
     * @param patient the patient being checked
     * @param alerts the list where generated alerts are stored
     */
    @Override
    public void checkAlert(Patient patient, List<Alert> alerts)
    {
        List<PatientRecord> records = patient.getRecords(Long.MIN_VALUE, Long.MAX_VALUE);
        PatientRecord previousSat = null;
        for(PatientRecord current : records)
        {
            if(!current.getRecordType().equals("Saturation"))
                continue;
            if(current.getMeasurementValue() < 92)
            {
                alerts.add(factory.createAlert(String.valueOf(current.getPatientId()),
                         "Low Saturation", current.getTimestamp()));
            }
            if(previousSat != null)
            {
                double drop = previousSat.getMeasurementValue() - current.getMeasurementValue();
                long timeDifference = current.getTimestamp() - previousSat.getTimestamp();
                if(drop >= 5 && timeDifference <= 600000)
                {
                    alerts.add(factory.createAlert(String.valueOf(current.getPatientId()),
                             "Rapid Saturation Drop", current.getTimestamp()));
                }
            }
            previousSat = current;
        }
    }
}