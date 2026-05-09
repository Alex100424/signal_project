package com.cardio_generator;

import com.data_management.DataStorage;

/**
 * Entry point for the app
 * Runs either the data storage or the health data simulator
 */
public class Main {

    /**
     * Starts the selected part of the app
     * 
     * @param args command-line arguments
     * @throws Exception if the selected app fails to start
     */
    public static void main(String[] args) throws Exception {
        if(args.length > 0 && args[0].equals("DataStorage"))
            DataStorage.main(new String[] {});
        else
            HealthDataSimulator.main(args);
    }
}
