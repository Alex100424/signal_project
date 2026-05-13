package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.data_management.*;

class WebSocketIntegrationTest {

    /**
     * Tests that critical blood pressure message received is stored and later
     * triggers alert
     */
    @Test
    void testWebSocketDataTriggersBloodPressureAlert() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.saveMessage("1,1000,SystolicPressure,181", storage);
        Patient patient = storage.getAllPatients().get(0);
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);

        assertTrue(alertGenerator.getAlerts().stream().anyMatch(alert -> alert.getCondition().contains("Critical")));
    }

    /**
     * Tests that low saturation message is stored and later triggers alert
     */
    @Test
    void testWebSocketDataTriggersSaturationAlert() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.saveMessage("2,1000,Saturation,91%", storage);
        Patient patient = storage.getAllPatients().get(0);
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);

        assertTrue(alertGenerator.getAlerts().stream().anyMatch(alert -> alert.getCondition().contains("Saturation")));
    }

    /**
     * Tests triggered alert message is stored and later triggers alert
     */
    @Test
    void testWebSocketDataTriggersManualAlert() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.saveMessage("3,1000,Alert,triggered", storage);
        Patient patient = storage.getAllPatients().get(0);
        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);

        assertTrue(alertGenerator.getAlerts().stream().anyMatch(alert -> alert.getCondition().contains("Manual")));
    }

    /**
     * Tests  multiple messages for the same patient are appended
     */
    @Test
    void testMultipleWebSocketMessagesSamePatient() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.saveMessage("4,1000,Saturation,98%", storage);
        reader.saveMessage("4,2000,Saturation,93%", storage);

        assertEquals(1, storage.getAllPatients().size());
        assertEquals(2, storage.getRecords(4, 0, 3000).size());
    }
}