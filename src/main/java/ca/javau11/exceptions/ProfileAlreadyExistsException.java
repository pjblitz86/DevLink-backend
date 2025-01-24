package ca.javau11.exceptions;

public class ProfileAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProfileAlreadyExistsException(String message) {
        super(message);
    }
}
