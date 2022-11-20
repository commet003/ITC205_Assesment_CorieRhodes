package test;

import airlock.entities.IPressureSensor;
import airlock.entities.PressureSensor;
import airlock.exceptions.PressureException;

import static org.junit.Assert.assertEquals;



public class PressureSensorTest {

    IPressureSensor pressureSensor;
    IPressureSensor pressureSensor2;
    double initPressure = 2.1;
    double initPressure2;
    double newPressure = 5.4;
    double negativePressure = -1.2;

    @org.junit.Before
    public void setUp() throws Exception {
        pressureSensor = new PressureSensor(initPressure);
        //pressureSensor2 = new PressureSensor(Double.NaN);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        pressureSensor = null;

    }

    @org.junit.Test
    public void getPressure() throws PressureException {
        assertEquals(initPressure, pressureSensor.getPressure(), 0.001);
    }

    @org.junit.Test
    public void setPressure() throws PressureException {
        pressureSensor.setPressure(newPressure);
        assertEquals(newPressure, pressureSensor.getPressure(), 0.001);
    }

    @org.junit.Test(expected = PressureException.class)
    public void setNegativePressure() throws PressureException {
        pressureSensor.setPressure(negativePressure);
    }


}