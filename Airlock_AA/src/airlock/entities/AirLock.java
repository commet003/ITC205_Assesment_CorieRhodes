package airlock.entities;

import airlock.exceptions.AirLockException;
import airlock.exceptions.DoorException;
import airlock.exceptions.PressureException;

public class AirLock implements IAirLock{
	
	private IDoor outerDoor;
	private IDoor innerDoor;
	private IPressureSensor lockSensor;
	
	private AirLockState state;	
	private OperationMode mode;
	

	public AirLock(IDoor externalDoor, IDoor internalDoor, IPressureSensor lockSensor) {
		//TODO - implement method
	}
		
	@Override
	public void openOuterDoor() throws AirLockException {
		//TODO - implement method
	}
		
	@Override
	public void closeOuterDoor() throws AirLockException {
		//TODO - implement method
	}
	
	@Override
	public void openInnerDoor() throws AirLockException {
		//TODO - implement method
	}
	
	@Override
	public void closeInnerDoor() throws AirLockException {
		//TODO - implement method
	}
	
	@Override
	public void equaliseWithCabinPressure() throws AirLockException {
		//TODO - implement method
	}

	@Override
	public void equaliseWithEnvironmentPressure()  throws AirLockException {
		//TODO - implement method
	}

	@Override
	public void toggleOperationMode() throws AirLockException{
		//TODO - implement method
	}
	
	@Override
	public boolean isOuterDoorClosed() {
		//TODO - implement method
		return false;
	}

	@Override
	public boolean isInnerDoorClosed() {
		//TODO - implement method
		return false;
	}

	@Override
	public boolean isInManualMode() {
		return mode == OperationMode.MANUAL;
	}

	@Override
	public boolean isInAutoMode() {
		return mode == OperationMode.AUTO;
	}

	@Override
	public boolean isSealed() {
		return state == AirLockState.SEALED;
	}

	@Override
	public boolean isUnsealed() {
		return state == AirLockState.UNSEALED;
	}

	public String toString() {
		return String.format(
			"Airlock: state: %s, mode: %s", 
			state, mode);
	}
	

}
