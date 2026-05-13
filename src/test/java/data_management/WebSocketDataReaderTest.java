package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import com.data_management.*;

class WebSocketDataReaderTest {

    /**
     * Tests that a normal message is parsed and stored corectly
     */
    @Test
    void testSaveMessage() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.saveMessage("1,1000,Saturation,95%", storage);

        List<PatientRecord> records = storage.getRecords(1, 0, 2000);

        assertEquals(1, records.size());
        assertEquals(1, records.get(0).getPatientId());
        assertEquals(1000, records.get(0).getTimestamp());
        assertEquals("Saturation", records.get(0).getRecordType());
        assertEquals(95.0, records.get(0).getMeasurementValue());
    }

    /**
     * Tests that the reader converts a triggered alert message into 1
     */
    @Test
    void testSaveMessageConvertsTriggeredAlert() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.saveMessage("2,2000,Alert,triggered", storage);

        List<PatientRecord> records = storage.getRecords(2, 0, 3000);

        assertEquals(1, records.size());
        assertEquals("Alert", records.get(0).getRecordType());
        assertEquals(1.0, records.get(0).getMeasurementValue());
    }

    /**
     * Tests that the reader converts a resolved alert message into 0
     */
    @Test
    void testSaveMessageConvertsResolvedAlert() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.saveMessage("2,3000,Alert,resolved", storage);
        List<PatientRecord> records = storage.getRecords(2, 0, 4000);

        assertEquals(1, records.size());
        assertEquals("Alert", records.get(0).getRecordType());
        assertEquals(0.0, records.get(0).getMeasurementValue());
    }

    /**
     * Tests that bad mesages with missing fields are rejected
     */
    @Test
    void testSaveMessageRejectsMissingFields() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        assertThrows(RuntimeException.class, () -> reader.saveMessage("1,1000,Saturation", storage));
        assertTrue(storage.getAllPatients().isEmpty());
    }

    /**
     * Tests that empty messages are rejected
     */
    @Test
    void testSaveMessageRejectsEmpty() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        assertThrows(
                IllegalArgumentException.class,
                () -> reader.saveMessage("", storage));

        assertTrue(storage.getAllPatients().isEmpty());
    }

}