package test;
import java.io.PrintStream;

import org.junit.Before;
import org.mockito.*;

import airlock.entities.AirLock;
import airlock.entities.IAirLock;
import airlock.exceptions.AirLockException;

/*
 *******************************************
 *** This class is an automated test     ***
 *** That tests the Airlock as the user  ***
 *** Passes through the Airlock from the ***
 *** outside into the cabin              ***
 *******************************************
 */
// FAT Script: 1.1
// Authored by: Corie Rhodes
// Date: 08/12/2022

public class FATLowerEnvironmentPressure {

    double exteriorPressure = 0.5;
    double lockPressure = 1.0;
    double interiorPressure = 1.0;

    PrintStream sysErr;

    IAirLock airLock = Mockito.mock(IAirLock.class);
    
    @Before
    void setup() {
        sysErr = System.err;

        try {
            airLock.openOuterDoor();
        } catch (AirLockException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }
}
