package airlock.entities;

import airlock.exceptions.PressureException;

public class PressureSensor implements IPressureSensor {
	
	double pressure;
	
	public PressureSensor(double initialPressure) throws PressureException {
		//TODO - implement method
	}
	
	public double getPressure() {
		//TODO - implement method
		return 0.0;
	}
	
	public void setPressure(double newPressure) throws PressureException {
		//TODO - implement method
	}

	public String toString() {
		return String.format(
			"PressureSensor: pressure: %3.1f bar", getPressure());
	}

}
