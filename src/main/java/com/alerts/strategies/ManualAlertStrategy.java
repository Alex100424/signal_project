package com.alerts.strategies;

import java.util.*;
import com.alerts.Alert;
import com.alerts.factories.ManualAlertFactory;
import com.data_management.*;

/**
 * Strategy for checking manually triggered alerts
 */
public class ManualAlertStrategy implements AlertStrategy {

    private ManualAlertFactory factory = new ManualAlertFactory();

    /**
     * Checks alert records for manually triggered alerts
     *
     * @param patient the patient whose data is checked
     * @param alerts the list where generated alerts are stored
     */
    @Override
    public void checkAlert(Patient patient, List<Alert> alerts)
    {
        List<PatientRecord> records = patient.getRecords(Long.MIN_VALUE, Long.MAX_VALUE);
        List<PatientRecord> alertRecords = new ArrayList<>();
        for(PatientRecord record : records)
        {
            if(record.getRecordType().equals("Manual Triggered Alert"))
                alertRecords.add(record);
        }
        alertRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));
        for(PatientRecord record : alertRecords)
        {
            if(record.getMeasurementValue() == 1.0)
            {
                Alert alert = factory.createAlert(String.valueOf(record.getPatientId()),
                         "Manual Triggered Alert", record.getTimestamp());
                alerts.add(alert);
            }
        }
    }
}