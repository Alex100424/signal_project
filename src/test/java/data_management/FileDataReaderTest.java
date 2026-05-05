package data_management;

import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.data_management.*;

class FileDataReaderTest {
    @Test
    void testReadData() throws Exception
    {
        Path temporaryDirectory = Files.createTempDirectory("testOutput"); //creates temporary folder
        Path saturationF = temporaryDirectory.resolve("Saturation.txt"); //creates a path for a file inside the temporary fodler
        Files.writeString(saturationF, "Patient ID: 3, Timestamp: 3000, Label: Saturation, Data: 50%\n"); //writes a line in the temp file

        DataStorage storage = new DataStorage();
        FileDataReader reader = new FileDataReader(temporaryDirectory.toString());
        reader.readData(storage);
        List<PatientRecord> records = storage.getRecords(3, 0, 5000);
        //checks that everything was read correcly
        assertEquals(1, records.size());
        assertEquals("Saturation", records.get(0).getRecordType());
        assertEquals(3000, records.get(0).getTimestamp());
        assertEquals(50.0, records.get(0).getMeasurementValue());
    }
}
