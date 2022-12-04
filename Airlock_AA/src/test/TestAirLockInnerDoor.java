package test;

import airlock.entities.*;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAirLockInnerDoor {

    IAirLock airLock;
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
        innerDoorExSensor = new PressureSensor(2.0);
        lockSensor = new PressureSensor(1.0);
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        outerDoor = new Door(outerDoorExSensor, lockSensor, outerDoorState);
        innerDoor = new Door(innerDoorExSensor, lockSensor, innerDoorState);
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
    }

    // Test 1: test that exception is thrown if inner door is already open
    @Test
    public void testOpenInnerDoorAlreadyOpen() {
        try {
            airLock.openInnerDoor();
        } catch (AirLockException e) {
            assertThrows(AirLockException.class, () -> airLock.openInnerDoor());
        }
    }

    // Test 2: ensure if mode is auto & outer door is open, outer door is closed
    // before opening inner door opened
    @Test
    void testInnerDoorOpenAutoMode() throws AirLockException {
        if (airLock.isInManualMode()) {
            airLock.toggleOperationMode();
            airLock.openOuterDoor();
        }

        airLock.openInnerDoor();
        assertTrue(!airLock.isInnerDoorClosed() && airLock.isOuterDoorClosed());
    }

    // Test 3: test that pressure is equalized when inner door is closed
    @Test
    void testPressureEqualized() throws AirLockException {
        if (airLock.isInManualMode()) {
            airLock.toggleOperationMode();
        }
        airLock.openInnerDoor();
        assertEquals(lockSensor.getPressure(), innerDoorExSensor.getPressure());
    }

    // Test 4: test that innerDoor is opened after pressure equalization
    @Test
    void testInnerDoorOpened() throws AirLockException {
        if (airLock.isInManualMode()) {
            airLock.toggleOperationMode();
        }
        airLock.openInnerDoor();
        // Returns true if inner door is open and pressure is equalized with cabin
        assertTrue(!airLock.isInnerDoorClosed() && lockSensor.getPressure() == innerDoorExSensor.getPressure());
    }

    // Test 5: test that if mode is manual, attempt made to open inner door
    @Test
    void testInnerDoorOpenManualMode() {
        if (airLock.isInManualMode()) {
            assertThrows(AirLockException.class, () -> airLock.openInnerDoor());
        }
    }

    // Test 6: test if no exceptions thrown door is opened and airlock is unsealed
    @Test
    void testUnsealedIfNoExceptions() {
        if (lockSensor.getPressure() != innerDoorExSensor.getPressure()) {
            try {
                airLock.equaliseWithCabinPressure();
            } catch (AirLockException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            airLock.openInnerDoor();
        } catch (AirLockException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(airLock.isUnsealed());
    }

    // Test 7: test if exception is thrown, and airlock was already sealed, that it
    // remains sealed
    @Test
    void testAirlockRemainSealed() {
        if (airLock.isSealed()) {
            try {
                airLock.openInnerDoor();
            } catch (AirLockException e) {
                assertTrue(airLock.isSealed());
            }
        }
    }

    // Test 8: test that door exceptions are encapsulated and thrown as
    // AirLockExceptions
    @Test
    void testDoorExceptionsEncapsulated() {
        try {
            airLock.openOuterDoor();
        } catch (AirLockException e) {
            assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
        }
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
