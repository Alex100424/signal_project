package com.cardio_generator.outputs;

/**
 * Interface for classes that are responsible for where the data is sent.
 */
public interface OutputStrategy {
    /**
     * Outputs one generated record.
     * 
     * @param patientId ID of the patient
     * @param timestamp the time when the data was generated
     * @param label the type of data
     * @param data the generated value
     */
    void output(int patientId, long timestamp, String label, String data);
}
