package airlock.exceptions;

public class AirLockException extends Exception {

	private static final long serialVersionUID = 1L;

	public AirLockException(String message) {
		super(message);
	}

	public AirLockException(Exception e) {
		super(e);
	}
}
