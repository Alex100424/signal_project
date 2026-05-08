package data_management;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    /**
     * Tests that a critical systolic blood pressure alert is generated
     * when the systolic presure is above 180 mmHg
     */
    @Test
    void testHighSystolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 181, "SystolicPressure", 1000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Critical SystolicPressure"));
    }

    /**
     * Tests that a critical systolic blood pressure alert is generated
     * when the systolic pressure is below 90 mHg
     */
    @Test
    void testLowSystolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 89, "SystolicPressure", 1000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Critical SystolicPressure"));
    }

    /**
     * Tests that a critical diastolic blood pressure alert is generated
     * when the diastolic pressure is above 120 mmHg
     */
    @Test
    void testHighDiastolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 121, "DiastolicPressure", 1000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Critical DiastolicPressure"));
    }

    /**
     * Tests that a critical diastolic blood pressure alert is generated
     * when the diastolic presure is below 60 mmHg
     */
    @Test
    void testLowDiastolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 59, "DiastolicPressure", 1000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Critical DiastolicPressure"));
    }

    /**
     * Tests that an increasing systolic blood pressure trend alert is generated
     * when three consecutive systolic readings increase by more than 10 mmHg each
     */
    @Test
    void testIncreasingSystolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 100, "SystolicPressure", 1000);
        storage.addPatientData(1, 112, "SystolicPressure", 2000);
        storage.addPatientData(1, 125, "SystolicPressure", 3000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Increasing SystolicPressure Trend"));
    }

    /**
     * Tetss that a decreasing systolic blood pressure trend alert is generated
     * when three consecutive systolic readings decrease by more than 10 mmHg each
     */
    @Test
    void testDecreasingSystolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 130, "SystolicPressure", 1000);
        storage.addPatientData(1, 115, "SystolicPressure", 2000);
        storage.addPatientData(1, 100, "SystolicPressure", 3000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Decreasing SystolicPressure Trend"));
    }

    /**
     * Tests that an increasing diastolic blood pressure trend alert is generatd
     * when three consecutive diastolic readings increase by more than 10 mmHg each
     */
    @Test
    void testIncreasingDiastolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 70, "DiastolicPressure", 1000);
        storage.addPatientData(1, 82, "DiastolicPressure", 2000);
        storage.addPatientData(1, 95, "DiastolicPressure", 3000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Increasing DiastolicPressure Trend"));
    }

    /**
     * Tests that a decreasing diastolic blood pressure trend alert is generated
     * when three consecutive diastolic readings decrease by more than 10 mmHg each
     */
    @Test
    void testDecreasingDiastolicBloodPressureAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(1, 100, "DiastolicPressure", 1000);
        storage.addPatientData(1, 85, "DiastolicPressure", 2000);
        storage.addPatientData(1, 70, "DiastolicPressure", 3000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Decreasing DiastolicPressure Trend"));
    }

    /**
     * Tests that a low saturation alert is generated when blood oxygen saturation
     * falls below 92%
     */
    @Test
    void testLowSaturationAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(2, 91, "Saturation", 1000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Low Saturation"));
    }

    /**
     * Tests that a rapid saturation drop alert is generated when saturation
     * drops by 5% or more within a 10min interval
     */
    @Test
    void testSaturationDropAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(2, 98, "Saturation", 1000);
        storage.addPatientData(2, 93, "Saturation", 1000 + 9 * 60 * 1000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Rapid Saturation Drop"));
    }

    /**
     * Tests that a Hypotensive Hypoxemia Alert is generated when both systolic
     * blood pressure is below 90 mHg and saturation is below 92%
     */
    @Test
    void testHypotensiveHypoxemiaAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(3, 89, "SystolicPressure", 1000);
        storage.addPatientData(3, 91, "Saturation", 2000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Hypotensive Hypoxemia Alert"));
    }

    /**
     * Tests that an abnormal ECG peak alert is generated when the current exg
     * value is far above the sliding-window average
     */
    @Test
    void testAbnormalEcgAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(4, 0.10, "ECG", 1000);
        storage.addPatientData(4, 0.11, "ECG", 2000);
        storage.addPatientData(4, 0.12, "ECG", 3000);
        storage.addPatientData(4, 0.10, "ECG", 4000);
        storage.addPatientData(4, 0.11, "ECG", 5000);
        storage.addPatientData(4, 1.00, "ECG", 6000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Abnormal ECG Peak"));
    }

    /**
     * Tests that a trigered alert is generated when the alert record has value 1
     */
    @Test
    void testTriggeredAlert() {
        DataStorage storage = new DataStorage();

        storage.addPatientData(5, 1.0, "Alert", 1000);

        AlertGenerator generator = new AlertGenerator(storage);
        Patient patient = storage.getAllPatients().get(0);

        generator.evaluateData(patient);

        assertTrue(hasAlert(generator.getAlerts(), "Manual Triggered Alert"));
    }

    private boolean hasAlert(List<Alert> alerts, String condition) {
        for (Alert alert : alerts) {
            if (alert.getCondition().contains(condition)) {
                return true;
            }
        }

        return false;
    }
}