package com.alerts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.factories.*;
import com.alerts.strategies.*;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> alerts;
    private BloodPressureAlertFactory bloodPressureAlertFactory;
    private BloodOxygenAlertFactory bloodOxygenAlertFactory;
    private ECGAlertFactory ecgAlertFactory;
    private ManualAlertFactory manualAlertFactory;
    private List<AlertStrategy> strategies;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
        this.bloodPressureAlertFactory = new BloodPressureAlertFactory();
        this.bloodOxygenAlertFactory = new BloodOxygenAlertFactory();
        this.ecgAlertFactory = new ECGAlertFactory();
        this.manualAlertFactory = new ManualAlertFactory();
        this.strategies = new ArrayList<>();
        strategies.add(new BloodPressureStrategy());
        strategies.add(new OxygenSaturationStrategy());
        strategies.add(new ECGStrategy());
        strategies.add(new ManualAlertStrategy());
    }

    /**
     * Returns the list of alerts. Used for tests
     * 
     * @return the list of generated alerts
     */
    public List<Alert> getAlerts() {
        return alerts;
    }

    /**
     * Removes all stored alerts from the AlertGenerator. Used for tessts
     */
    public void clearAlerts() {
        alerts.clear();
    }

    /**
     * Returns all records of a specific type for a given patient
     * Record typesȘ "SystolicPressure", "DiastolicPressure", "Saturation", "ECG",
     * "Alert"
     * 
     * @param patient    the patient
     * @param recordType the type of record
     * @return a sorted list of records matching record type
     */
    private List<PatientRecord> getRecordsByType(Patient patient, String recordType) {
        List<PatientRecord> records = patient.getRecords(Long.MIN_VALUE, Long.MAX_VALUE);
        List<PatientRecord> filteredRecords = new ArrayList<>();

        for (PatientRecord record : records) {
            if (record.getRecordType().equals(recordType)) {
                filteredRecords.add(record);// add records to list
            }
        }

        filteredRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));// sorts records by timestamp oldest
                                                                                    // to newest
        return filteredRecords;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        if (patient == null)
            return;

        checkBloodPressureAlerts(patient);
        checkSaturationAlerts(patient);
        checkHypotensiveHypoxemiaAlert(patient);
        checkEcgAlerts(patient);
        checkTriggeredAlerts(patient);

        for (AlertStrategy strategy : strategies)
            strategy.checkAlert(patient, alerts);

    }

    /**
     * Checks all blood pressure alert conditions for the patient
     * Systolic pressure is critical if <90 or >180
     * Diastolic pressure is critical if <60 or >120
     * 
     * @param patient
     */
    private void checkBloodPressureAlerts(Patient patient) {
        List<PatientRecord> systolicRecords = getRecordsByType(patient, "SystolicPressure");
        List<PatientRecord> diastolicRecords = getRecordsByType(patient, "DiastolicPressure");

        checkCriticalBloodPressure(systolicRecords, "SystolicPressure", 90, 180);
        checkCriticalBloodPressure(diastolicRecords, "DiastolicPressure", 60, 120);

        checkBloodPressureTrend(systolicRecords, "SystolicPressure");
        checkBloodPressureTrend(diastolicRecords, "DiastolicPressure");
    }

    /**
     * Checks if blood pressure is outside the range
     * 
     * @param records blood pressure records
     * @param type    type of blood pressure("SystolicPressure" or
     *                "DiastolicPressure")
     * @param min     lower threshold
     * @param max     uper threshold
     */
    private void checkCriticalBloodPressure(List<PatientRecord> records, String type, double min,
            double max) {
        for (PatientRecord record : records) {
            double value = record.getMeasurementValue();

            if (value > max || value < min) {
                Alert alert = bloodPressureAlertFactory.createAlert(String.valueOf(record.getPatientId()),
                        "Critical " + type, record.getTimestamp());

                triggerAlert(alert);
            }
        }

    }

    /**
     * Checks if 3 consecutive blood pressure readings show a increasing or
     * decreasing trend
     * 
     * @param records blood pressure records
     * @param type    type of blood pressure being checked("SystolicPressure" or
     *                "DiastolicPressure")
     */
    private void checkBloodPressureTrend(List<PatientRecord> records, String type) {
        for (int i = 2; i < records.size(); i++) {
            double val1 = records.get(i - 2).getMeasurementValue();
            double val2 = records.get(i - 1).getMeasurementValue();
            double val3 = records.get(i).getMeasurementValue();

            double change1 = val2 - val1; // change from second to first measurement
            double change2 = val3 - val2; // change from third to second measurement

            boolean increasingTrend = (change1 > 10) && (change2 > 10);
            boolean decreasingTrend = (change1 < -10) && (change2 < -10);

            if (increasingTrend) {
                Alert alert = bloodPressureAlertFactory.createAlert(String.valueOf(records.get(i).getPatientId()),
                        "Increasing " + type + " Trend", records.get(i).getTimestamp());
                triggerAlert(alert);
            }

            if (decreasingTrend) {
                Alert alert = bloodPressureAlertFactory.createAlert(String.valueOf(records.get(i).getPatientId()),
                        "Decreasing " + type + " Trend", records.get(i).getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Checks blood oxygen saturation alert conditions for the patient
     * 
     * @param patient the patient
     */
    private void checkSaturationAlerts(Patient patient) {
        List<PatientRecord> saturationRecords = getRecordsByType(patient, "Saturation");

        checkLowSaturation(saturationRecords);
        checkSaturationDrop(saturationRecords);
    }

    /**
     * Checks if anu blood oxygen saturation reading is below 92%
     * 
     * @param records saturation records
     */
    private void checkLowSaturation(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getMeasurementValue() < 92) {
                Alert alert = bloodOxygenAlertFactory.createAlert(String.valueOf(record.getPatientId()),
                        "Low Saturation", record.getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Checks if blood oxygen saturation drops rapidly in a 10min interval
     * 
     * @param records saturation records
     */
    private void checkSaturationDrop(List<PatientRecord> records) {
        int timeInterval = 600000; // 10min in milisec

        for (int i = 1; i < records.size(); i++) {
            PatientRecord prev = records.get(i - 1);
            PatientRecord curr = records.get(i);

            double drop = prev.getMeasurementValue() - curr.getMeasurementValue(); // the drop in saturation
            double timeDifference = curr.getTimestamp() - prev.getTimestamp();

            if (drop >= 5 && timeDifference <= timeInterval) {
                Alert alert = bloodOxygenAlertFactory.createAlert(String.valueOf(curr.getPatientId()),
                        "Rapid Saturation Drop", curr.getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Checks if the patient has a combined hypotensive hypoxemia condition
     * 
     * @param patient the patient
     */
    private void checkHypotensiveHypoxemiaAlert(Patient patient) {
        List<PatientRecord> systolicRecords = getRecordsByType(patient, "SystolicPressure");
        List<PatientRecord> saturationRecords = getRecordsByType(patient, "Saturation");

        if (systolicRecords.isEmpty() || saturationRecords.isEmpty())
            return;

        PatientRecord systolic = systolicRecords.get(systolicRecords.size() - 1); // get last record
        PatientRecord saturation = saturationRecords.get(saturationRecords.size() - 1);

        boolean lowBloodPressure = systolic.getMeasurementValue() < 90;
        boolean lowSaturation = saturation.getMeasurementValue() < 92;

        if (lowBloodPressure && lowSaturation) {
            long alertTime = Math.max(systolic.getTimestamp(), saturation.getTimestamp());

            Alert alert = bloodOxygenAlertFactory.createAlert(String.valueOf(systolic.getPatientId()),
                    "Hypotensive Hypoxemia Alert", alertTime);
            triggerAlert(alert);
        }
    }

    /**
     * Checks if the ecg data contains an abnormal peak
     * 
     * @param patient the patient
     */
    private void checkEcgAlerts(Patient patient) {
        List<PatientRecord> ecgRecords = getRecordsByType(patient, "ECG");

        int window = 5; // widnow size, look at 5 readings

        for (int i = window; i < ecgRecords.size(); i++) {
            double sum = 0;

            for (int j = i - window; j < i; j++) {
                sum += ecgRecords.get(j).getMeasurementValue();
            }

            double avg = sum / window;
            double currentVal = ecgRecords.get(i).getMeasurementValue();

            if (currentVal > avg * 2) {
                Alert alert = ecgAlertFactory.createAlert(String.valueOf(ecgRecords.get(i).getPatientId()),
                        "Abnormal ECG Peak", ecgRecords.get(i).getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Checks if a bedside alert was triggered
     * 
     * @param patient the patient
     */
    private void checkTriggeredAlerts(Patient patient) {
        List<PatientRecord> alertRecords = getRecordsByType(patient, "Alert");

        for (PatientRecord record : alertRecords) {
            if (record.getMeasurementValue() == 1.0) { // triggered = 1, resolved = 0
                Alert alert = manualAlertFactory.createAlert(String.valueOf(record.getPatientId()),
                        "Manual Triggered Alert", record.getTimestamp());

                triggerAlert(alert);
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        alerts.add(alert);
    }
}
