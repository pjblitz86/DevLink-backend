package ca.javau11.exceptions;

public class PostAlreadyLikedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PostAlreadyLikedException(String message) {
        super(message);
    }
}