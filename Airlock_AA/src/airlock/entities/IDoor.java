package airlock.entities;

import airlock.exceptions.DoorException;

public interface IDoor {
	
	void open()  throws DoorException;
	void close() throws DoorException;
	
	double getExternalPressure();
	double getInternalPressure();
	
	boolean isOpen();
	boolean isClosed();
	
}
