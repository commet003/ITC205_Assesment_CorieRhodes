package airlock.entities;

import airlock.exceptions.PressureException;

public class PressureSensor implements IPressureSensor {
	
	double pressure;
	
	public PressureSensor(double initialPressure) throws PressureException {
		//Checks if initialPressure is negative, else sets pressure to initialPressure
		if (initialPressure < 0) {
			throw new PressureException("Pressure cannot be negative");
		}else{
			pressure = initialPressure;
		}

	}
	
	public double getPressure() {
		//returns current pressure value
		return pressure;
	}
	
	public void setPressure(double newPressure) throws PressureException {
		//Checks if newPressure is negative, else sets pressure to newPressure
		if (newPressure < 0) {
			throw new PressureException("The new value for Pressure cannot be negative");
		}else{
			pressure = newPressure;
		}
	}

	public String toString() {
		return String.format(
			"PressureSensor: pressure: %3.1f bar", getPressure());
	}

}
