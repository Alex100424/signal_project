package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for classes that generate simulated data for a patient
 */
public interface PatientDataGenerator {
    /**
     * Generates data for one patient and send it to the output strategy.
     * 
     * @param patientId ID of the patient
     * @param outputStrategy the output method used to publish the generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
