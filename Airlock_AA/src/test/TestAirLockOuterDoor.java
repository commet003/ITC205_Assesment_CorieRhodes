package test;

import airlock.entities.*;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class TestAirLockOuterDoor {

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
        outerDoorExSensor = new PressureSensor(2.0);
        innerDoorExSensor = new PressureSensor(1.0);
        lockSensor = new PressureSensor(1.0);
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        outerDoor = new Door(outerDoorExSensor, lockSensor, outerDoorState);
        innerDoor = new Door(innerDoorExSensor, lockSensor, innerDoorState);
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
    }

    // Test 1: Check that openOuterDoor throws exception if outer door is already
    // opened
    @Test
    public void testOpenOuterDoorAlreadyOpen() {
        try {
            airLock.openOuterDoor();
        } catch (AirLockException e) {
            assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
        }

    }

    // Test 2: ensure if mode is auto & inner door is open, inner door is closed
    // before opening outer door
    @Test
    void testOuterDoorOpenAutoMode() throws AirLockException {
        if (airLock.isInManualMode()) {
            airLock.toggleOperationMode();
            airLock.openInnerDoor();
        }

        airLock.openOuterDoor();
        assertTrue(airLock.isInnerDoorClosed() && !airLock.isOuterDoorClosed());
    }

    // Test 3: test that pressure is equalized when inner door is closed
    @Test
    void testPressureEqualized() throws AirLockException {
        if (airLock.isInManualMode()) {
            airLock.toggleOperationMode();
        }
        airLock.openOuterDoor();
        assertEquals(lockSensor.getPressure(), outerDoorExSensor.getPressure());
    }

    // Test 4: test that outerDoor is opened after pressure equalization
    @Test
    void testOuterDoorOpened() throws AirLockException {
        if (airLock.isInManualMode()) {
            airLock.toggleOperationMode();
        }
        airLock.openOuterDoor();
        assertTrue(!airLock.isOuterDoorClosed() && lockSensor.getPressure() == outerDoorExSensor.getPressure());
    }

    // Test 5: test that if mode is manual, attempt made to open outer door
    @Test
    void testOuterDoorOpenManualMode() {
        if (airLock.isInManualMode()) {
            assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
        }
    }

    // Test 6: test if no exceptions thrown door is opened and airlock is unsealed
    @Test
    void testUnsealedIfNoExceptions() {
        if (lockSensor.getPressure() != outerDoorExSensor.getPressure()) {
            try {
                airLock.equaliseWithEnvironmentPressure();
            } catch (AirLockException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            airLock.openOuterDoor();
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
                airLock.openOuterDoor();
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
