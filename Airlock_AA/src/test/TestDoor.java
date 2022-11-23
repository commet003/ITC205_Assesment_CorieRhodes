package test;

import airlock.entities.*;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import airlock.entities.OperationMode;

import static org.junit.Assert.*;

class DoorTest {
    // Doors
    IDoor door;

    // Pressure sensors
     IPressureSensor internalPressureSensor;
     IPressureSensor externalPressureSensor;

    // Door states
     DoorState state;

    // Door Mode
     OperationMode mode;

    // Ensure that the constructor throws a DoorException if the supplied sensors are not valid implementations of the IPressureSensor interface.
    @Test
    void testPressureNotValid(){
        state = DoorState.CLOSED;
        assertThrows(DoorException.class, () -> door = new Door(externalPressureSensor, internalPressureSensor, state));
    }

    // Ensure that the constructor throws an DoorException if the initial Door state is set to OPEN but the difference between internal and external pressures is greater than TOLERANCE.
    @Test
    void testPressureDifferenceTooHigh() throws PressureException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(1);
        state = DoorState.OPEN;

        assertThrows(DoorException.class, () -> door = new Door(externalPressureSensor, internalPressureSensor, state));
    }

    // Ensure that if no exceptions are thrown, that the constructor returns a valid fully initialised Door. (i.e. no other methods should fail unexpectedly or give unspecified results if the constructor is successful
    @Test
    void testConstructor() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.OPEN;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        Assertions.assertNotNull(door);
    }

    // ensure that openDoor throws a DoorException if open() is called when the Door is already open
    @Test
    void testOpenDoorAlreadyOpen() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.OPEN;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        assertThrows(DoorException.class, () -> door.open());
    }

    // ensure that openDoor throws an DoorException if the Door is closed and the difference between external and internal pressures is greater than TOLERANCE
    @Test
    void testOpenDoorPressureDifferenceTooHigh() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(1);
        state = DoorState.CLOSED;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        assertThrows(DoorException.class, () -> door.open());
    }

    // ensure that if no exceptions are thrown that the door is opened (i.e. that the Door’s state becomes OPEN)
    @Test
    void testOpenDoor() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.CLOSED;
        door = new Door(externalPressureSensor, internalPressureSensor, state);
        door.open();
        Assertions.assertTrue(door.isOpen());
    }

    // ensure that closeDoor throws a DoorException if close() is called when the Door is already closed
    @Test
    void testCloseDoorAlreadyClosed() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.CLOSED;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        assertThrows(DoorException.class, () -> door.close());
    }

    // ensure that if no exception is thrown that the Door’s state becomes CLOSED
    @Test
    void testCloseDoor() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.OPEN;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        door.close();
        Assertions.assertTrue(door.isClosed());
    }

    // Ensure that getExternalPressure returns the correct pressure value associated with the external pressure sensor set by the constructor
    @Test
    void testGetExternalPressure() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(4);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.CLOSED;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        Assertions.assertEquals(5, door.getExternalPressure(), 0.001);
    }

    // Ensure that getInternalPressure returns the correct pressure value associated with the internal pressure sensor set by the constructor
    @Test
    void testGetInternalPressure() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(4);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.CLOSED;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        Assertions.assertEquals(4, door.getInternalPressure(), 0.001);
    }

    // Ensure that isOpen returns true when the Door is OPEN and false when it is CLOSED
    @Test
    void testIsOpenTrue() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.OPEN;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        Assertions.assertTrue(door.isOpen());
    }

    @Test
    void testIsOpenFalse() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(4);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.CLOSED;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        Assertions.assertFalse(door.isOpen());
    }

    // Ensure that isClosed returns true when the Door is CLOSED and false when it is OPEN
    @Test
    void testIsClosedTrue() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(4);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.CLOSED;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        Assertions.assertTrue(door.isClosed());
    }

    @Test
    void testIsClosedFalse() throws PressureException, DoorException {
        // set internal and external pressure
        internalPressureSensor = new PressureSensor(5);
        externalPressureSensor = new PressureSensor(5);
        state = DoorState.OPEN;

        door = new Door(externalPressureSensor, internalPressureSensor, state);
        Assertions.assertFalse(door.isClosed());
    }

    // Cleans up after each test has been run
    @After
    public void tearDown(){

        internalPressureSensor = null;
        externalPressureSensor = null;
        state = null;
        mode = null;


        System.out.println("Test completed");
    }
}