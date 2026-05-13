package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.data_management.*;

class WebSocketErrorHandlingTest {

    /**
     * Tests that a bad message doesnt get stored
     */
    @Test
    void testCorruptedMessageDoesntChangeStorage() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        assertThrows(IllegalArgumentException.class, () -> reader.saveMessage("corrupted-message", storage));
        assertTrue(storage.getAllPatients().isEmpty());
    }

    /**
     * Tests that bad timestamp data is rejected
     */
    @Test
    void testInvalidTimestampRejected() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        assertThrows(IllegalArgumentException.class,() -> reader.saveMessage("1,wrongTime,Saturation,95%", storage));
        assertTrue(storage.getAllPatients().isEmpty());
    }

    /**
     * Tests that bad measurement values are rejected
     */
    @Test
    void testInvalidMeasurementValueRejected() {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        assertThrows(IllegalArgumentException.class,() -> reader.saveMessage("1,1000,Saturation,notANumber", storage));
        assertTrue(storage.getAllPatients().isEmpty());
    }
}