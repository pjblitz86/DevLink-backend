package ca.javau11.exceptions;

public class PostNotLikedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PostNotLikedException(String message) {
        super(message);
    }
}
