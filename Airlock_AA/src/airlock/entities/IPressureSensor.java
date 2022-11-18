package airlock.entities;

import airlock.exceptions.PressureException;

public interface IPressureSensor {
	
	public double getPressure();
	public void setPressure(double newPressure) throws PressureException;

}
