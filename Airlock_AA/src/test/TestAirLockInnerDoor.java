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
        innerDoorExSensor = new PressureSensor(1.0);
        lockSensor = new PressureSensor(1.0);
        outerDoorState = DoorState.CLOSED;
        innerDoorState = DoorState.CLOSED;
        outerDoor = new Door(outerDoorExSensor, lockSensor, outerDoorState);
        innerDoor = new Door(innerDoorExSensor, lockSensor, innerDoorState);
        airLock = new AirLock(outerDoor, innerDoor, lockSensor);
    }


     /* Ensure that all DoorExceptions,
      * are caught and then rethrown encapsulated in AirLockExceptions
      */
    

    //TEST innerDoorOpen with lockSensor Pressure > or < cabin pressure
    @Test
    void testInnerDoorOpen() throws AirLockException {
        try{
            airLock.toggleOperationMode();
            lockSensor.setPressure(2.5);
        }catch(PressureException e){
            throw new AirLockException(e.getMessage());
        }
        airLock.openInnerDoor();
        assertFalse(airLock.isInnerDoorClosed());
    }

    @Test
    void testInnerDoorOpenLessThan() throws AirLockException {
        try{
            airLock.toggleOperationMode();
            lockSensor.setPressure(0.5);
        }catch(PressureException e){
            throw new AirLockException(e.getMessage());
        }
        airLock.openInnerDoor();
        assertFalse(airLock.isInnerDoorClosed());
    }

    // Ensure that openInnerDoor throws an AirLockException if openInnerDoor,
    // is called while the inner door is already open.
    @Test
    void testOpenInnerDoorAlreadyOpen() throws AirLockException {
        airLock.openInnerDoor();
        assertThrows(AirLockException.class, () -> airLock.openInnerDoor());
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

    /*  Ensure that all DoorExceptions are caught,
     *  and then rethrown encapsulated in AirLockExceptions
     */
    @Test
    void ifDoorExAirlockExInnerDoor() throws AirLockException {
        airLock.openInnerDoor();
        assertThrows(AirLockException.class, () -> airLock.openInnerDoor());
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
