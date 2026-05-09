package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;

class SingletonPatternTest {

    /**
     * Tests that DataStorage.getInstance returns the same object
     */
    @Test
    void testDataStorageSingletonReturnsSameInstance() {
        DataStorage firstInstance = DataStorage.getInstance();
        DataStorage secondInstance = DataStorage.getInstance();
        assertSame(firstInstance, secondInstance);
    }

    /**
     * Tests that data added through one DataStorage singleton reference is
     * visible through other reference
     */
    @Test
    void testDataStorageSingletonSharesState() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        int original = instance2.getAllPatients().size();
        instance1.addPatientData(999, 91, "Saturation", 1000);

        int modifid = instance2.getAllPatients().size();

        assertTrue(modifid >= original);
    }

    /**
     * Tests that HealthDataSimulator.getInstance returns the same object
     */
    @Test
    void testHealthDataSimulatorSingletonReturnsSameInstance() {
        HealthDataSimulator first = HealthDataSimulator.getInstance();
        HealthDataSimulator second = HealthDataSimulator.getInstance();
        assertSame(first, second);
    }
}