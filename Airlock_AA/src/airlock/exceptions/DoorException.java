package airlock.exceptions;

public class DoorException extends Exception {

	private static final long serialVersionUID = 1L;

	public DoorException(String message) {
		super(message);
	}

	public DoorException(Exception e) {
		super(e);
	}

}
