package airlock.entities;
import airlock.exceptions.PressureException;

import static org.testng.AssertJUnit.assertEquals;

public class PressureSensorTest {

    IPressureSensor pressureSensor;
    double initPressure = 2.1;
    double newPressure = 5.4;
    double negativePressure = -1.2;
    PressureException pressureException;

    @org.junit.Before
    public void setUp() throws Exception {
        pressureSensor = new PressureSensor(initPressure);

    }

    @org.junit.After
    public void tearDown() throws Exception {
        pressureSensor = null;

    }

    @org.junit.Test
    public void getPressure() {
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