package alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.alerts.Alert;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.alerts.factories.ManualAlertFactory;

/**
 * Unit tests for alert factories
 */
class AlertFactoryTest {

    /**
     * Tests that the blood pressure factory creates an alert correctly
     */
    @Test
    void testBloodPressureAlertFactory()
    {
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "Critical SystolicPressure", 1500);

        assertEquals("1", alert.getPatientId());
        assertEquals("Critical SystolicPressure", alert.getCondition());
        assertEquals(1500, alert.getTimestamp());
    }

    /**
     * Tests that the blood oxygen factory creates an alert correctly
     */
    @Test
    void testBloodOxygenAlertFactory()
    {
        BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("2", "Low Saturation", 2700);

        assertEquals("2", alert.getPatientId());
        assertEquals("Low Saturation", alert.getCondition());
        assertEquals(2700, alert.getTimestamp());
    }

    /**
     * Tests that the ECG factory creates an alert correctly
     */
    @Test
    void testEcgAlertFactory()
    {
        ECGAlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("3", "Abnormal ECG Peak", 3900);

        assertEquals("3", alert.getPatientId());
        assertEquals("Abnormal ECG Peak", alert.getCondition());
        assertEquals(3900, alert.getTimestamp());
    }

    /**
     * Tests that the manual alert factory creates an alert correctly
     */
    @Test
    void testManualAlertFactory()
    {
        ManualAlertFactory factory = new ManualAlertFactory();
        Alert alert = factory.createAlert("4", "Manual Triggered Alert", 4200);

        assertEquals("4", alert.getPatientId());
        assertEquals("Manual Triggered Alert", alert.getCondition());
        assertEquals(4200, alert.getTimestamp());
    }
}