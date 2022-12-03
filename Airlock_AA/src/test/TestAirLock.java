package test;

import airlock.entities.*;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    // Ensure that a valid fully initialised AirLock is returned.
    // (Both airlock state and operation mode are initialised correctly)
    @Test
    void testValidAirLock() {
        assertInstanceOf(AirLock.class, this.airLock);
    }

    // Ensure that initial airlock state is set to SEALED if both doors are CLOSED
    @Test
    void testAirLockSeal() {
        // Initialise airlock
        assertTrue(airLock.isSealed());
    }

    // and otherwise UNSEALED
    @Test
    void testAirLockUnsealed() throws AirLockException {
        airLock.openOuterDoor();
        assertTrue(airLock.isUnsealed());
    }

    // Ensure that initial operational mode is set to MANUAL.
    @Test
    void testAirLockManual() {
        // Initialise airlock
        assertTrue(airLock.isInManualMode());
    }

    /*
     **
     ** TESTING openOuterDoor method
     **
     */

    // Ensure that openOuterDoor throws an AirLockException
    // if openOuterDoor is called while the outer door is already open.
    @Test
    void testOpenOuterDoorAlreadyOpen() throws AirLockException {
        airLock.openOuterDoor();
        assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
    }

    // Ensure that if operation mode is AUTO and the inner door
    // is open then an attempt is made to close the inner door
    // before the outer door is opened.
    @Test
    void testOpenOuterDoorAutoInnerOpen() throws AirLockException {
        airLock.toggleOperationMode();
        airLock.openInnerDoor();
        airLock.openOuterDoor();
        assertTrue(airLock.isInnerDoorClosed());
    }

    // Ensure that if operation mode is AUTO and after the inner door is closed
    // then an attempt is made to equalise pressures with the external environment
    @Test
    void ifModeAutoAttemptEqualiseExternal() throws AirLockException {
        airLock.toggleOperationMode();
        airLock.openInnerDoor();
        airLock.openOuterDoor();
        assertTrue(airLock.isInnerDoorClosed());
    }

    // Ensure that if operation mode is AUTO,
    // and after the inner door is closed,
    // and pressure has been equalised with the external environment,
    // that an attempt is made to open the outer door
    @Test
    void ifModeAutoAttemptOpenOuterDoor() throws AirLockException {
        airLock.toggleOperationMode();
        airLock.openInnerDoor();
        airLock.openOuterDoor();
        assertTrue(airLock.isInnerDoorClosed());
        assertFalse(airLock.isOuterDoorClosed());
    }

    // Ensure that if operation mode is MANUAL,
    // then an attempt is made to open the outer door
    @Test
    void ifModeManualAttemptOpenOuterDoor() throws AirLockException {
        if(airLock.isInManualMode()){
            airLock.openOuterDoor();
            assertFalse(airLock.isOuterDoorClosed());
        }else{
            airLock.toggleOperationMode();
            airLock.openOuterDoor();
            assertFalse(airLock.isOuterDoorClosed());
        }

    }

    // Ensure that if no ex exceptions are thrown,
    // that the airlock state becomes UNSEALED
    @Test
    void ifNoExAirlockUnsealed() throws AirLockException {
        airLock.openOuterDoor();
        assertTrue(airLock.isUnsealed());
    }

    // Ensure that if any exceptions are thrown and the airlock was SEALED,
    // when openOuterDoor was called, that the airlock remains SEALED.
    @Test
    void ifExAirlockSealed() throws AirLockException {
        airLock.openOuterDoor();
        assertTrue(airLock.isSealed());
    }

    // Ensure that all DoorExceptions,
    // are caught and then rethrown encapsulated in AirLockExceptions
    @Test
    void ifDoorExAirlockEx() throws AirLockException {
        airLock.openOuterDoor();
        assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
    }

    /*
     **
     ** TESTING openOuterDoor method
     **
     */

    // Ensure that openInnerDoor throws an AirLockException if openInnerDoor,
    // is called while the inner door is already open.
    @Test
    void testOpenInnerDoorAlreadyOpen() throws AirLockException {
        airLock.openInnerDoor();
        assertThrows(AirLockException.class, () -> airLock.openInnerDoor());
    }

    // Ensure that if operation mode is AUTO and the outer door,
    // is open then an attempt is made to close the outer door
    @Test
    void testOpenInnerDoorAutoOuterOpen() throws AirLockException {
        airLock.toggleOperationMode();
        airLock.openOuterDoor();
        airLock.openInnerDoor();
        assertTrue(airLock.isOuterDoorClosed());
    }

    // Ensure that if operation mode is AUTO and after the outer door,
    // is closed then an attempt is made to equalise pressures,
    // with the internal cabin
    @Test
    void ifModeAutoAttemptEqualiseInternal() throws AirLockException {
        if (airLock.isInAutoMode()) {
            airLock.openOuterDoor();
            airLock.openInnerDoor();
            assertTrue(airLock.isOuterDoorClosed());
        } else {
            airLock.toggleOperationMode();
            airLock.openOuterDoor();
            airLock.openInnerDoor();
            assertTrue(airLock.isOuterDoorClosed());
        }
    }

    // Ensure that if operation mode is AUTO,
    // and after the outer door is closed and pressure has been equalised,
    // with the internal cabin that an attempt is made to open the inner door
    @Test
    void ifModeAutoAttemptOpenInnerDoor() throws AirLockException {
        if (airLock.isInAutoMode()) {
            if (airLock.isOuterDoorClosed()) {
                airLock.openInnerDoor();
                assertFalse(airLock.isInnerDoorClosed());
            }
        }
    }

    // Ensure that if operation mode is MANUAL then an attempt is made to open the inner door
    @Test
    void ifModeManualAttemptOpenInnerDoor() throws AirLockException {

        airLock.openInnerDoor();
        assertFalse(airLock.isInnerDoorClosed());
    }

    // Ensure that if no ex exceptions are thrown that the airlock state becomes UNSEALED
    @Test
    void ifNoExAirlockUnsealedInnerDoor() throws AirLockException {
        airLock.openInnerDoor();
        assertTrue(airLock.isUnsealed());
    }

    // Ensure that if any exceptions are thrown and the airlock was SEALED when,
    // openInnerDoor was called, that the airlock remains SEALED.
    @Test
    void ifExAirlockSealedInnerDoor(){
        try{
            airLock.openInnerDoor();
            assertFalse(airLock.isSealed());
        }catch (AirLockException e){
            assertTrue(airLock.isSealed());
        }

    }

    // Ensure that all DoorExceptions are caught,
    // and then rethrown encapsulated in AirLockExceptions
    @Test
    void ifDoorExAirlockExInnerDoor() throws AirLockException {
        airLock.openInnerDoor();
        assertThrows(AirLockException.class, () -> airLock.openInnerDoor());
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