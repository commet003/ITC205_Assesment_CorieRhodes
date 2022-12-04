package test;

import airlock.entities.*;
import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAirLock {

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

    // Test negative pressure set
    @Test
    void testSetNegativePressure() throws PressureException {
        assertThrows(PressureException.class, () -> {
            lockSensor.setPressure(-2.3);
        });
    }

    // Ensure that a valid fully initialised AirLock is returned.
    // (Both airlock state and operation mode are initialised correctly)
    @Test
    void testValidAirLock() {
        assertInstanceOf(AirLock.class, this.airLock);
    }

    // Test init getPressure() method    
    @Test
    void testGetPressure() throws PressureException {
        assertEquals(1.0, lockSensor.getPressure());
    }

    // test getPressure() method after setPressure() method
    @Test
    void testGetUpdatedPressure() throws PressureException {
        lockSensor.setPressure(2.0);
        assertEquals(2.0, lockSensor.getPressure());
    }

    // Ensure that initial airlock state is set to SEALED if both doors are CLOSED
    @Test
    void testAirLockSealed() {
        if(airLock.isInnerDoorClosed() && airLock.isOuterDoorClosed()) {
            assertTrue(airLock.isSealed());
        }else{
            assertFalse(airLock.isSealed());
        }
    }

    // and otherwise UNSEALED
    @Test
    void testAirLockUnsealed() throws AirLockException {
        airLock.openOuterDoor();
        // Check if airlock is unsealed if either door is open
        if(!airLock.isInnerDoorClosed() || !airLock.isOuterDoorClosed()) {
            assertTrue(airLock.isUnsealed());
        }
    }

    // Ensure that initial operational mode is set to MANUAL.
    @Test
    void testAirLockManual() {
        assertTrue(airLock.isInManualMode());
    }

    // Ensure that if no exceptions are thrown,
    // that the airlock state becomes UNSEALED
    @Test
    void ifNoExceptionAirlockUnsealed(){
        try{
            airLock.openOuterDoor();
            assertTrue(airLock.isUnsealed());
        }catch(AirLockException e){
            System.out.println(e.getMessage());
        }
    }

    // ensure that setPressure updates the initial pressure set by the constructor
    @Test
    void testSetPressure() throws PressureException {
        lockSensor.setPressure(2.3);
        assertEquals(2.3, lockSensor.getPressure());
    }

    // Test exception thrown if pressure is set to negative value
    @Test
    void testSetNegativePressureException() throws PressureException {
        assertThrows(PressureException.class, () -> {
            lockSensor.setPressure(-2.3);
        });
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