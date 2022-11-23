package test;

import airlock.entities.IPressureSensor;
import airlock.entities.PressureSensor;
import airlock.exceptions.PressureException;
import org.junit.*;

import static org.junit.Assert.*;


public class TestPressureSensor {

    IPressureSensor pressureSensor;
    double initPressure;
    double initPressure2;
    double newPressure;
    double negativePressure;


    @After
    public void tearDown(){
        pressureSensor = null;
        newPressure = 0.0;
        initPressure = 0.0;
        initPressure2 = 0.0;
        negativePressure = 0.0;

    }

    // ensure that the constructor throws an ‘InvalidPressureException’
    // if an attempt is made to initialise the sensor with a negative pressure (< 0.0)
    @Test
    public void testConstructorNegativePressure() {
        negativePressure = -1.0;
        assertThrows(PressureException.class, () -> pressureSensor = new PressureSensor(negativePressure));
    }


    // ensure that the constructor returns a valid fully initialised PressureSensor object.
    // (i.e. no other methods should fail unexpectedly or give unspecified results if the constructor is successful)

    @Test
    public void testConstructor() throws PressureException {
        initPressure = 5.0;
        pressureSensor = new PressureSensor(initPressure);
        assertNotNull(pressureSensor);
    }

    // ensure that getPressure returns the initial pressure set in the constructor
    @Test
    public void testGetPressure() throws PressureException {
        initPressure = 5.0;
        pressureSensor = new PressureSensor(initPressure);
        assertEquals(initPressure, pressureSensor.getPressure(), 0.01);
    }

    // ensure that getPressure returns any updated pressure set by setPressure
    @Test
    public void testSetPressure() throws PressureException {
        initPressure = 5.0;
        newPressure = 10.0;
        pressureSensor = new PressureSensor(initPressure);
        pressureSensor.setPressure(newPressure);
        assertEquals(newPressure, pressureSensor.getPressure(), 0.01);
    }

    //ensure that setPressure throws an ‘InvalidPressureException’
    // if an attempt is made to set a negative pressure (< 0.0)
    @Test
    public void testSetPressureNegativePressure() throws PressureException {
        initPressure = 5.0;
        negativePressure = -1.0;
        newPressure = negativePressure;
        pressureSensor = new PressureSensor(initPressure);
        assertThrows(PressureException.class, () -> pressureSensor.setPressure(newPressure));
    }

    // ensure that setPressure updates the initial pressure set by the constructor
    @Test
    public void testSetPressureUpdatesInitialPressure() throws PressureException {
        initPressure = 5.0;
        newPressure = 10.0;
        pressureSensor = new PressureSensor(initPressure);
        pressureSensor.setPressure(newPressure);
        assertEquals(newPressure, pressureSensor.getPressure(), 0.01);
    }




}