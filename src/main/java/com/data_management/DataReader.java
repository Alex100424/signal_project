package com.data_management;

import java.io.IOException;

public interface DataReader extends AutoCloseable {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Stops the reader
     * 
     * @throws IOException if reader cant be stopped cleanly
     */
    default void stop() throws IOException
    {
        close();
    }

    /**
     * Vloses any resources used by the reder
     * 
     * @throws IOException if resources cant be closed
     */
    @Override
    default void close() throws IOException
    {
        //Default - no resources to close
    }
}
