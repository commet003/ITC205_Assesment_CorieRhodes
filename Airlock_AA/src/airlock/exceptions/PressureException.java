package airlock.exceptions;

public class PressureException extends Exception {

	private static final long serialVersionUID = 1L;

	public PressureException(String message) {
		super(message);
	}

	public PressureException(Exception e) {
		super(e);
	}	

}
