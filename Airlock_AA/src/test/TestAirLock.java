package test;

import airlock.entities.*;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestAirLock {

    static IAirLock airLock;
    static IDoor outerDoor;
    static IDoor innerDoor;
    static IPressureSensor lockSensor;
    static IPressureSensor outerDoorExSensor;
    static IPressureSensor innerDoorExSensor;
    static DoorState outerDoorState;

    static DoorState innerDoorState;

    @BeforeEach
    public void setUp() throws PressureException, DoorException {
        outerDoorExSensor = new PressureSensor(1.0);
        innerDoorExSensor = new PressureSensor(1.0);
        lockSensor = new PressureSensor(1.0);
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        outerDoor = new Door(outerDoorExSensor, lockSensor, outerDoorState);
        innerDoor = new Door(innerDoorExSensor, lockSensor, innerDoorState);
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
    }

    //Ensure that a valid fully initialised AirLock is returned.
    // (Both airlock state and operation mode are initialised correctly)
    @Test
    void testValidAirLock(){
        // Initialise airlock
        //airLock = new AirLock(outerDoor, innerDoor, lockSensor);
        assertInstanceOf(AirLock.class, airLock);
    }

    // Ensure that initial airlock state is set to SEALED if both doors are CLOSED
    @Test
    void testAirLockSeal() {
        // Initialise airlock
        assertTrue(airLock.isSealed());
    }
    //  and otherwise UNSEALED
    @Test
    void testAirLockUnsealed() throws AirLockException {
        airLock.openOuterDoor();
        assertTrue(airLock.isUnsealed());
    }

    // Ensure that initial operational mode is set to MANUAL.
    @Test
    void testAirLockManual(){
        // Initialise airlock
        assertTrue(airLock.isInManualMode());
    }


    /*
     **
     ** TESTING openOuterDoor method
     **
     */


    // Ensure that openOuterDoor throws an AirLockException if openOuterDoor is called while the outer door is already open.
    @Test
    void testOpenOuterDoorAlreadyOpen() throws AirLockException {
        airLock.openOuterDoor();
        assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
    }


    // Ensure that if operation mode is AUTO and the inner door is open then an attempt is made to close the inner door

    @Test
    void testOpenOuterDoorAutoInnerOpen() throws AirLockException {
        airLock.openInnerDoor();
        airLock.toggleOperationMode();
        airLock.openOuterDoor();
        System.out.println(airLock.isInnerDoorClosed());
        assertTrue(airLock.isInnerDoorClosed());
    }




    @AfterEach
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