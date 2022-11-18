package airlock.entities;

import airlock.exceptions.DoorException;

public interface IDoor {
	
	public void open()  throws DoorException;
	public void close() throws DoorException;
	
	public double getExternalPressure();
	public double getInternalPressure();
	
	public boolean isOpen();
	public boolean isClosed();
	
}
