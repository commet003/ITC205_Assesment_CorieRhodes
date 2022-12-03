package test;

import airlock.entities.*;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        outerDoorExSensor = new PressureSensor(1.0);
        innerDoorExSensor = new PressureSensor(1.0);
        lockSensor = new PressureSensor(1.0);
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        outerDoor = new Door(outerDoorExSensor, lockSensor, outerDoorState);
        innerDoor = new Door(innerDoorExSensor, lockSensor, innerDoorState);
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
    }


    // Ensure that openOuterDoor throws an AirLockException
    // if openOuterDoor is called while the outer door is already open.
    @Test
    void testOpenOuterDoorAlreadyOpen() throws AirLockException {
        airLock.openOuterDoor();
        assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
    }
    
    /*
     * Check that pressure is equalised correctly,
     * when lock pressure is greater than environment pressure
     */
    @Test
    void testOuterDoorOpen() throws AirLockException {
        try{
            airLock.toggleOperationMode();
            lockSensor.setPressure(2.5);
        }catch(PressureException e){
            throw new AirLockException(e.getMessage());
        }
        airLock.openOuterDoor();
        assertFalse(airLock.isOuterDoorClosed());
    }

    @Test
    void testOuterDoorOpenLessThan() throws AirLockException {
        try{
            airLock.toggleOperationMode();
            lockSensor.setPressure(0.5);
        }catch(PressureException e){
            throw new AirLockException(e.getMessage());
        }
        airLock.openOuterDoor();
        assertFalse(airLock.isOuterDoorClosed());
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


    @Test
    void ifDoorExAirlockEx() throws AirLockException {
        airLock.openOuterDoor();
        assertThrows(AirLockException.class, () -> airLock.openOuterDoor());
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
