package com.data_management;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.*;

/**
 * Reads patient data from simulator output files
 */
public class FileDataReader implements DataReader {
    private String directoryPath; // Directory containing output files
    public FileDataReader(String directoryPath)
    {
        this.directoryPath = directoryPath;
    }

    /**
     * Reads all the files in the directory and stores the data in DataStorage
     * 
     * @param dataStorage the storage where the records are saved
     * @throws IOException if the files aren't read properly
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException
    {
        for(Path file : Files.newDirectoryStream(Path.of(directoryPath)))
        {
            for(String line : Files.readAllLines(file))
            {
                String[] parts = line.split(", ");
                // Extract all the information
                int patientID = Integer.parseInt(parts[0].replace("Patient ID: ", ""));
                long timestamp = Long.parseLong(parts[1].replace("Timestamp: ", ""));
                String label = parts[2].replace("Label: ", "");
                String data = parts[3].replace("Data: ", "");
                
                double value;
                //Converts alert text and % values into numbers
                if(data.equals("triggered"))
                    value = 1.0;
                else if(data.equals("resolved"))
                    value = 0.0;
                else
                    value = Double.parseDouble(data.replace("%", ""));
                dataStorage.addPatientData(patientID, value, label, timestamp); // Stores the parsed record
            }
        }
    }
}
