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
		// Sets door and sensor dependencies
		this.outerDoor = externalDoor;
		this.innerDoor = internalDoor;
		this.lockSensor = lockSensor;

		// Sets the initial operation state to MANUAL
		this.mode = OperationMode.MANUAL;

		// If both doors are CLOSED then, sets initial airlock state to SEALED
		if(this.innerDoor.isClosed() && this.outerDoor.isClosed()) {
			this.state = AirLockState.SEALED;
		}
		// Otherwise, sets initial airlock state to UNSEALED
		else {
			this.state = AirLockState.UNSEALED;
		}
	}
		
	@Override
	public void openOuterDoor() throws AirLockException {
		// if outer door is already open, then throw an AirLockException reporting door is open
		if(this.outerDoor.isOpen()) {
			throw new AirLockException("Outer door is already open");
		}else{
			// try, if operation mode is AUTO then close inner door
			try{
				if(this.mode == OperationMode.AUTO) {
					if (this.innerDoor.isOpen()){
						this.innerDoor.close();
					}
					this.equaliseWithEnvironmentPressure();
				}
				// open outer door
				this.outerDoor.open();
				// set airlock state to UNSEALED
				this.state = AirLockState.UNSEALED;
			} catch (DoorException e){
				throw new AirLockException("Inner door is already closed " + e.getMessage());
			}
		}
	}
		
	@Override
	public void closeOuterDoor() throws AirLockException {
		try{
			this.outerDoor.close();
			if(this.innerDoor.isClosed()){
				this.state = AirLockState.SEALED;
			}

		}catch (DoorException e){
			throw new AirLockException("Outer door is already closed " + e.getMessage());
		}
	}
	
	@Override
	public void openInnerDoor() throws AirLockException {
		// if inner door is already open then throw AirLockException reporting door is open
		if(this.innerDoor.isOpen()) {
			throw new AirLockException("Inner door is already open");
		}else{
			try{
				// if operation mode is AUTO
				if(this.mode == OperationMode.AUTO) {
					// if outer door is open
					if (this.outerDoor.isOpen()){
						// close outer door
						this.outerDoor.close();
					}
					// equalise pressure
					this.equaliseWithCabinPressure();
				}
				this.innerDoor.open();
				this.state = AirLockState.UNSEALED;
			}catch (DoorException e){
				throw new AirLockException("Inner door is already closed " + e.getMessage());
			}
		}
	}
	
	@Override
	public void closeInnerDoor() throws AirLockException {
		try{
			this.innerDoor.close();
			if(this.outerDoor.isClosed()){
				this.state = AirLockState.SEALED;
			}
		} catch (DoorException e) {
			throw new AirLockException("DoorException thrown: " + e);
		}
	}
	
	@Override
	public void equaliseWithCabinPressure() throws AirLockException {
		// if airlock state is not SEALED then throw AirLockException reporting airlock is not sealed
		if(this.state != AirLockState.SEALED) {
			throw new AirLockException("Airlock is not sealed");
		}else{
			try {
				this.lockSensor.setPressure(this.innerDoor.getInternalPressure());
			} catch (PressureException e) {
				throw new AirLockException("PressureException thrown: " + e);
			}
		}
	}

	@Override
	public void equaliseWithEnvironmentPressure()  throws AirLockException {
		// if airlock state is not SEALED then throw AirLockException reporting airlock is not sealed
		if(this.state != AirLockState.SEALED) {
			throw new AirLockException("Airlock is not sealed");
		}else{
			try {
				this.lockSensor.setPressure(this.outerDoor.getExternalPressure());
			} catch (PressureException e) {
				throw new AirLockException("PressureException thrown: " + e);
			}
		}
	}

	@Override
	public void toggleOperationMode() throws AirLockException{
		// if airlock state is not SEALED then throws AirLockException
		if(this.state != AirLockState.SEALED) {
			throw new AirLockException("Airlock is not sealed");
		}else{
			// otherwise toggles operationState between MANUAL and AUTO
			if(this.mode == OperationMode.MANUAL) {
				this.mode = OperationMode.AUTO;
			}else{
				this.mode = OperationMode.MANUAL;
			}
		}
	}
	
	@Override
	public boolean isOuterDoorClosed() {

		return outerDoor.isClosed();
	}

	@Override
	public boolean isInnerDoorClosed() {

		return innerDoor.isClosed();
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
