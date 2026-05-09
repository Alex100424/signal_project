package com.alerts.strategies;

import java.util.*;
import com.alerts.Alert;
import com.alerts.factories.ECGAlertFactory;
import com.data_management.*;

/**
 * Strategy for checking ECG alerts
 */
public class ECGStrategy implements AlertStrategy {

    private ECGAlertFactory factory = new ECGAlertFactory();

    /**
     * Checks ECG data for peaks that are not normal
     *
     * @param patient the patient whose data is checked
     * @param alerts the list where generated alerts are stored
     */
    @Override
    public void checkAlert(Patient patient, List<Alert> alerts)
    {
        List<PatientRecord> records = patient.getRecords(Long.MIN_VALUE, Long.MAX_VALUE);
        List<PatientRecord> ecgRecords = new ArrayList<>();
        for(PatientRecord record : records)
        {
            if(record.getRecordType().equals("ECG"))
                ecgRecords.add(record);
        }
        ecgRecords.sort(Comparator.comparingLong(PatientRecord :: getTimestamp));
        int previousCount = 5;
        for(int i = previousCount; i < ecgRecords.size(); i++)
        {
            double sum = 0;
            for(int j = i - previousCount; j < i; j++)
            {
                sum += ecgRecords.get(j).getMeasurementValue();
            }
            double avg = sum / previousCount;
            double currentVal = ecgRecords.get(i).getMeasurementValue();
            if(currentVal > avg * 2)
            {
                Alert alert = factory.createAlert(String.valueOf(ecgRecords.get(i).getPatientId()),
                         "Abnormal ECG Peak", ecgRecords.get(i).getTimestamp());
                alerts.add(alert);
            }
        }
    }
}