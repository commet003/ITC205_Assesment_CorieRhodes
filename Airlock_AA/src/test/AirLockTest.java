package test;

import airlock.entities.*;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AirLockTest {

    IAirLock airLock;
    IDoor outerDoor;
    IDoor innerDoor;
    IPressureSensor lockSensor;
    IPressureSensor exSensor;
    IPressureSensor innerSensor;
    DoorState outerDoorState;
    DoorState innerDoorState;

    AirLock airlockValidSealedSetUp() throws PressureException, DoorException {
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        exSensor = new PressureSensor(1.0);
        innerSensor = new PressureSensor(1.0);
        outerDoor = new Door(exSensor, innerSensor, outerDoorState);
        innerDoor = new Door(exSensor, innerSensor, innerDoorState);
        lockSensor = new PressureSensor(5.0);
        return new AirLock(outerDoor, innerDoor, lockSensor);
    }

    AirLock airlockValidUnsealedSetUp() throws PressureException, DoorException {
        outerDoorState = DoorState.OPEN;
        innerDoorState = DoorState.CLOSED;
        exSensor = new PressureSensor(1.0);
        innerSensor = new PressureSensor(1.0);
        outerDoor = new Door(exSensor, innerSensor, outerDoorState);
        innerDoor = new Door(exSensor, innerSensor, innerDoorState);
        lockSensor = new PressureSensor(5.0);
        return new AirLock(outerDoor, innerDoor, lockSensor);
    }


    //Ensure that a valid fully initialised AirLock is returned.
    // (Both airlock state and operation mode are initialised correctly)
    @Test
    void testValidAirLock() throws DoorException, PressureException {
        // Initialise airlock
        this.airLock = airlockValidSealedSetUp();
        assertInstanceOf(AirLock.class, this.airLock);
        assertTrue(airLock.isInManualMode());
        assertTrue(airLock.isSealed());
    }

    // Ensure that initial airlock state is set to SEALED if both doors are CLOSED
    @Test
    void testAirLockSealed() throws DoorException, PressureException {
        // Initialise airlock
        this.airLock =  airlockValidUnsealedSetUp();
        assertFalse(airLock.isSealed());
    }
    //  and otherwise UNSEALED
    @Test
    void testAirLockUnsealed() throws DoorException, PressureException {
        // Init Doors, PressureSensor
        outerDoorState = DoorState.OPEN;
        innerDoorState = DoorState.CLOSED;
        exSensor = new PressureSensor(1.0);
        innerSensor = new PressureSensor(1.0);
        outerDoor = new Door(exSensor, innerSensor, outerDoorState);
        innerDoor = new Door(exSensor, innerSensor, innerDoorState);
        lockSensor = new PressureSensor(5.0);

        airLock = new AirLock(outerDoor, innerDoor, lockSensor);

        assertTrue(airLock.isUnsealed());

    }

    // Ensure that initial operational mode is set to MANUAL.
    @Test
    void testAirLockManual() throws DoorException, PressureException {
        // Init Doors, PressureSensor
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        exSensor = new PressureSensor(1.0);
        innerSensor = new PressureSensor(1.0);
        outerDoor = new Door(exSensor, innerSensor, outerDoorState);
        innerDoor = new Door(exSensor, innerSensor, innerDoorState);
        lockSensor = new PressureSensor(5.0);

        // Initialise airlock
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
        assertTrue(airLock.isInManualMode());
    }

    @AfterEach
    void tearDown() {
        // Set all variables to null
        airLock = null;
        outerDoor = null;
        innerDoor = null;
        lockSensor = null;
        exSensor = null;
        innerSensor = null;
        outerDoorState = null;
        innerDoorState = null;
    }
}