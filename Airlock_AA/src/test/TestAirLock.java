package test;

import airlock.entities.*;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestAirLock {

    IAirLock airLock;
    static IDoor outerDoor;
    static IDoor innerDoor;
    static IPressureSensor lockSensor;
    static IPressureSensor outerDoorExSensor;
    static IPressureSensor innerDoorExSensor;
    static DoorState outerDoorState;

    static DoorState innerDoorState;

    @BeforeAll
    public static void setUp() throws PressureException, DoorException {
        outerDoorExSensor = new PressureSensor(1.0);
        innerDoorExSensor = new PressureSensor(1.0);
        lockSensor = new PressureSensor(1.0);
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        outerDoor = new Door(outerDoorExSensor, lockSensor, outerDoorState);
        innerDoor = new Door(innerDoorExSensor, lockSensor, innerDoorState);
    }

    //Ensure that a valid fully initialised AirLock is returned.
    // (Both airlock state and operation mode are initialised correctly)
    @Test
    void testValidAirLock(){
        // Initialise airlock
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
        assertInstanceOf(AirLock.class, airLock);
    }

    // Ensure that initial airlock state is set to SEALED if both doors are CLOSED
    @Test
    void testAirLockSealed(){
        // Initialise airlock
        this.airLock =  new AirLock(outerDoor, innerDoor, lockSensor);
        assertTrue(this.airLock.isSealed());
    }
    //  and otherwise UNSEALED
    @Test
    void testAirLockUnsealed() throws DoorException {
        innerDoor.open(); // Opening the inner door should result
        // in the airlock being unsealed
        this.airLock = new AirLock(outerDoor, innerDoor, lockSensor);
        assertTrue(this.airLock.isUnsealed());
    }

    // Ensure that initial operational mode is set to MANUAL.
    @Test
    void testAirLockManual(){
        // Initialise airlock
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
        assertTrue(this.airLock.isInManualMode());
    }


    /*
    **
    ** TESTING openOuterDoor method
    **
    */


    // Ensure that openOuterDoor throws an AirLockException if openOuterDoor is called while the outer door is already open.
    @Test
    void testOpenOuterDoorAlreadyOpen() throws AirLockException, PressureException, DoorException {
        outerDoorExSensor = new PressureSensor(1.0);
        outerDoorState = DoorState.OPEN;
        outerDoor = new Door(outerDoorExSensor, lockSensor, outerDoorState);
        airLock = new AirLock(outerDoor,innerDoor,lockSensor);
        assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
    }




    @After
    void tearDown() {
        // Set all variables to null
        outerDoor = null;
        innerDoor = null;
        lockSensor = null;
        outerDoorExSensor = null;
        innerDoorExSensor = null;
        outerDoorState = null;
        innerDoorState = null;
    }
}