package com.alerts.strategies;
import java.util.*;
import com.alerts.Alert;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.*;

/**
 * Strategy for checking blood pressure alerts
 */
public class BloodPressureStrategy implements AlertStrategy {
    
    private BloodPressureAlertFactory factory = new BloodPressureAlertFactory();

    /**
     * Checks blood pressure records for critical values and trends
     * 
     * @param patient the patient being checked
     * @param alerts the list where the generated alerts are stored
     */
    @Override
    public void checkAlert(Patient patient, List<Alert> alerts)
    {
        List<PatientRecord> records = patient.getRecords(Long.MIN_VALUE, Long.MAX_VALUE);
        checkCriticalValues(records, alerts);
        checkTrends(records, "DiastolicPressure", alerts);
        checkTrends(records, "SystolicPressure", alerts);
    }

    /**
     * Checks for critical blood pressure values
     * 
     * @param records the patient records
     * @param alerts the list where the generated alerts are stored
     */
    private void checkCriticalValues(List<PatientRecord> records, List<Alert> alerts)
    {
        for(PatientRecord record : records)
            {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();
            if(type.equals("SystolicPressure") && (value < 90 || value > 180))
            {
                alerts.add(factory.createAlert(String.valueOf(record.getPatientId()),
                         "Critical SystolicPressure",record.getTimestamp()));
            }

            if(type.equals("DiastolicPressure") && (value < 60 || value > 120))
            {
                alerts.add(factory.createAlert(String.valueOf(record.getPatientId()),
                 "Critical DiastolicPressure", record.getTimestamp()));
            }
        }
    }

    /**
     * Checks for increasing or decreasing blood pressure trends
     * 
     * @param records the patient records
     * @param type blood pressure type
     * @param alerts the list where generated alerts are stored
     */
    private void checkTrends(List<PatientRecord> records, String type, List<Alert> alerts)
    {
        PatientRecord first = null;
        PatientRecord second = null;
        for(PatientRecord current : records)
        {
            if(!current.getRecordType().equals(type))
                continue;
            if(first == null)
            {
                first = current;
                continue;
            }
            if(second == null)
            {
                second = current;
                continue;
            }
            double change1 = second.getMeasurementValue() - first.getMeasurementValue();
            double change2 = current.getMeasurementValue() - second.getMeasurementValue();
            if(change1 > 10 && change2 > 10)
            {
                alerts.add(factory.createAlert(String.valueOf(current.getPatientId()),
                         "Increasing " + type + " Trend",current.getTimestamp()));
            }
            if(change1 < -10 && change2 < -10)
            {
                alerts.add(factory.createAlert(String.valueOf(current.getPatientId()),
                         "Decreasing " + type + " Trend", current.getTimestamp()));
            }
            first = second;
            second = current;
        }
    }
}
