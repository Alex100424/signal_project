package data_management;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import com.data_management.*;
import org.junit.jupiter.api.Test;

class PatientTest {
    //Tests that getRecords returns only the records in the provided time frame
    @Test
    void testGetRecords()
    {
        Patient patient = new Patient(1);
        patient.addRecord(50, "Saturation", 1000);
        patient.addRecord(60, "Saturation", 2000);
        patient.addRecord(70, "Saturation", 3000);
        List<PatientRecord> records = patient.getRecords(500, 1001);
        assertEquals(1, records.size());
        assertEquals(50, records.get(0).getMeasurementValue());
        assertEquals("Saturation", records.get(0).getRecordType());
        assertEquals(1000, records.get(0).getTimestamp());
    }
}
