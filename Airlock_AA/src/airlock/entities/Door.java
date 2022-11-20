package airlock.entities;

import airlock.exceptions.DoorException;

public class Door implements IDoor{
	
	private static final double TOLERANCE = 0.001;
	
	IPressureSensor inSensor;
	IPressureSensor exSensor;
	
	private DoorState state;
	
	public Door(IPressureSensor exSensor, IPressureSensor inSensor, 
	            DoorState initialState) throws DoorException {
		// Check exSensor and inSensor are not null
		if (exSensor == null || inSensor == null) {
			throw new DoorException("Pressure sensors cannot be null");
		}else{
			this.exSensor = exSensor;
			this.inSensor = inSensor;
		}

		// Check initialState is not null
		if (initialState == null) {
			throw new DoorException("Initial state cannot be null");
		}else {
			this.state = initialState;
		}

		// if initialState is OPEN, check that the pressure sensors are equal
		if (initialState == DoorState.OPEN) {
			if (Math.abs(exSensor.getPressure() - inSensor.getPressure()) > TOLERANCE) {
				throw new DoorException("Pressure sensors must be equal when door is open, but were " + exSensor.getPressure() + " and " + inSensor.getPressure());
			}
		}

	}
	
	@Override
	public void open() throws DoorException {
		// Check that the door is not already open
		if (state == DoorState.OPEN) {
			throw new DoorException("Door is already open");
		} else if (state == DoorState.CLOSED) {
			// Check that the pressure sensors are equal
			if (Math.abs(exSensor.getPressure() - inSensor.getPressure()) > TOLERANCE) {
				throw new DoorException("Pressure sensors must be equal when door is open, but were " + exSensor.getPressure() + " and " + inSensor.getPressure());
			}else{
				state = DoorState.OPEN;
			}
		}
	}
	
	@Override
	public void close() throws DoorException {
		// Check that the door is not already closed
		if (state == DoorState.CLOSED) {
			throw new DoorException("Door is already closed");
		} else if (state == DoorState.OPEN) {
			state = DoorState.CLOSED;
		}
	}

	@Override
	public double getExternalPressure() {

		return exSensor.getPressure();
	}

	@Override
	public double getInternalPressure() {
		return inSensor.getPressure();
	}

	@Override
	public boolean isOpen() {
		return state.equals(DoorState.OPEN);
	}

	@Override
	public boolean isClosed() {
		return state.equals(DoorState.CLOSED);
	}

	public String toString() {
		return String.format(
			"Door: state: %s, external pressure: %3.1f bar, internal pressure: %3.1f bar", 
			state, exSensor.getPressure(), inSensor.getPressure());
	}	

}
