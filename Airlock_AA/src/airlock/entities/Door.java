package airlock.entities;

import airlock.exceptions.DoorException;

public class Door implements IDoor{
	
	private static double TOLERANCE = 0.001;
	
	IPressureSensor inSensor;
	IPressureSensor exSensor;
	
	private DoorState state;
	
	public Door(IPressureSensor exSensor, IPressureSensor inSensor, 
	            DoorState initialState) throws DoorException {
		//TODO - implement method
	}
	
	@Override
	public void open() throws DoorException {
		//TODO - implement method
	}
	
	@Override
	public void close() throws DoorException {
		//TODO - implement method
	}

	@Override
	public double getExternalPressure() {
		//TODO - implement method
		return 0.0;
	}

	@Override
	public double getInternalPressure() {
		//TODO - implement method
		return 0.0;
	}

	@Override
	public boolean isOpen() {
		return state == DoorState.OPEN;
	}

	@Override
	public boolean isClosed() {
		return state == DoorState.CLOSED;
	}

	public String toString() {
		return String.format(
			"Door: state: %s, external pressure: %3.1f bar, internal pressure: %3.1f bar", 
			state, exSensor.getPressure(), inSensor.getPressure());
	}	

}
