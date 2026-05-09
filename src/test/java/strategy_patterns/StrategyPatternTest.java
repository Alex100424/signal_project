package strategy_patterns;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.strategies.*;
import com.data_management.Patient;

class StrategyPatternTest {

    /**
     * Tests that BloodPressureStrategy creates a critical systolic presure aletr
     */
    @Test
    void testBloodPressureStrategySystolic() {
        Patient patient = new Patient(1);
        patient.addRecord(181, "SystolicPressure", 1000);

        List<Alert> alerts = new ArrayList<>();
        new BloodPressureStrategy().checkAlert(patient, alerts);
        assertTrue(hasAlert(alerts, "Critical SystolicPressure"));
    }

    /**
     * Tests that BloodPressureStrategy creates a critical diastolic pressure alert
     */
    @Test
    void testBloodPressureStrategyDiastolic() {
        Patient patient = new Patient(1);
        patient.addRecord(121, "DiastolicPressure", 1000);

        List<Alert> alerts = new ArrayList<>();
        new BloodPressureStrategy().checkAlert(patient, alerts);
        assertTrue(hasAlert(alerts, "Critical DiastolicPressure"));
    }

    /**
     * Tests that BloodPressureStrategy creates an increasing systoli alert
     */
    @Test
    void testBloodPressureStrategyIncreasingTrend() {
        Patient patient = new Patient(1);
        patient.addRecord(100, "SystolicPressure", 1000);
        patient.addRecord(112, "SystolicPressure", 2000);
        patient.addRecord(125, "SystolicPressure", 3000);

        List<Alert> alerts = new ArrayList<>();
        new BloodPressureStrategy().checkAlert(patient, alerts);

        assertTrue(hasAlert(alerts, "Increasing SystolicPressure Trend"));
    }

    /**
     * Tests that BloodPressureStrategy creates a decreasing systolic alert
     */
    @Test
    void testBloodPressureStrategyDecreasingTrend() {
        Patient patient = new Patient(1);
        patient.addRecord(130, "SystolicPressure", 1000);
        patient.addRecord(115, "SystolicPressure", 2000);
        patient.addRecord(100, "SystolicPressure", 3000);

        List<Alert> alerts = new ArrayList<>();
        new BloodPressureStrategy().checkAlert(patient, alerts);
        assertTrue(hasAlert(alerts, "Decreasing SystolicPressure Trend"));
    }

    /**
     * Tests that OxygenSaturationStrategy creates a low saturation alert
     */
    @Test
    void testOxygenSaturationStrategyLowSaturation() {
        Patient patient = new Patient(2);
        patient.addRecord(91, "Saturation", 1000);

        List<Alert> alerts = new ArrayList<>();
        new OxygenSaturationStrategy().checkAlert(patient, alerts);
        assertTrue(hasAlert(alerts, "Low Saturation"));
    }

    /**
     * Testt that OxygenSaturationStrategy creates a rapid saturation drop alert
     */
    @Test
    void testOxygenSaturationStrategyRapidDrop() {
        Patient patient = new Patient(2);
        patient.addRecord(98, "Saturation", 1000);
        patient.addRecord(93, "Saturation", 1000 + 9 * 60 * 1000);

        List<Alert> alerts = new ArrayList<>();
        new OxygenSaturationStrategy().checkAlert(patient, alerts);
        assertTrue(hasAlert(alerts, "Rapid Saturation Drop"));
    }

    /**
     * Tests that ECGStrategy creates a n alert for a ECG peak.
     */
    @Test
    void testEcgStrategyPeak() {
        Patient patient = new Patient(4);
        patient.addRecord(0.10, "ECG", 1000);
        patient.addRecord(0.11, "ECG", 2000);
        patient.addRecord(0.12, "ECG", 3000);
        patient.addRecord(0.10, "ECG", 4000);
        patient.addRecord(0.11, "ECG", 5000);
        patient.addRecord(1.00, "ECG", 6000);

        List<Alert> alerts = new ArrayList<>();
        new ECGStrategy().checkAlert(patient, alerts);
        assertTrue(hasAlert(alerts, "Abnormal ECG Peak"));
    }

    /**
     * Tests that ManualAlertStrategy creates alert when the record has val 1,
     */
    @Test
    void testAlertStrategyTriggered() {
        Patient patient = new Patient(5);
        patient.addRecord(1.0, "Alert", 1000);

        List<Alert> alerts = new ArrayList<>();
        new ManualAlertStrategy().checkAlert(patient, alerts);
        assertTrue(hasAlert(alerts, "Manual Triggered Alert"));
    }

    /**
     * Method that checks if a list of alerts has a condition
     *
     * @param alerts generated alerts
     * @param condition  condition text
     * @return true if one alert contains the expected condition text
     */
    private boolean hasAlert(List<Alert> alerts, String condition) {
        for (Alert alert : alerts) {
            if (alert.getCondition().contains(condition)) {
                return true;
            }
        }

        return false;
    }
}