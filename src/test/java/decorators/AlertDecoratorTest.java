package decorators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.decorators.*;

class AlertDecoratorTest {

    /**
     * Tests that PriorityAlertDecorator keeps the original patient info and has
     * priority decoration
     */
    @Test
    void testPriorityAlertDecorator() {
        Alert alert = new Alert("1", "Critical SystolicPressure", 1000);

        PriorityAlertDecorator decoratedAlert = new PriorityAlertDecorator(alert, "HIGH");

        assertEquals("1", decoratedAlert.getPatientId());
        assertEquals(1000, decoratedAlert.getTimestamp());
        assertEquals("HIGH", decoratedAlert.getPriority());
        assertTrue(decoratedAlert.getCondition().contains("Priority: HIGH"));
        assertTrue(decoratedAlert.getCondition().contains("Critical SystolicPressure"));
    }

    /**
     * Tests that RepeatedAlertDecorator keeps the original alert
     */
    @Test
    void testRepeatedAlertDecorator() {
        Alert alert = new Alert("2", "Low Saturation", 2000);

        RepeatedAlertDecorator decoratedAlert = new RepeatedAlertDecorator(alert, 3, 5000);

        assertEquals("2", decoratedAlert.getPatientId());
        assertEquals(2000, decoratedAlert.getTimestamp());
        assertEquals(3, decoratedAlert.getCount());
        assertEquals(5000, decoratedAlert.getInterval());
        assertTrue(decoratedAlert.getCondition().contains("Low Saturation"));
        assertTrue(decoratedAlert.getCondition().contains("Repeated 3 times"));
    }

    /**
     * Tests that decorators can be combined
     */
    @Test
    void testDecoratorsCombinaiton() {
        Alert alert = new Alert("3", "Abnormal ECG Peak", 3000);

        RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(alert, 2, 1000);
        PriorityAlertDecorator priorityRepeatedAlert = new PriorityAlertDecorator(repeatedAlert, "URGENT");

        assertEquals("3", priorityRepeatedAlert.getPatientId());
        assertEquals(3000, priorityRepeatedAlert.getTimestamp());
        assertTrue(priorityRepeatedAlert.getCondition().contains("Priority: URGENT"));
        assertTrue(priorityRepeatedAlert.getCondition().contains("Abnormal ECG Peak"));
        assertTrue(priorityRepeatedAlert.getCondition().contains("Repeated 2 times"));
    }
}