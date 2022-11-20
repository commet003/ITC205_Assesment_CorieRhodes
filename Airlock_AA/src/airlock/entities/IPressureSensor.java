package airlock.entities;

import airlock.exceptions.PressureException;

public interface IPressureSensor {
	
	double getPressure();
	void setPressure(double newPressure) throws PressureException;

}
