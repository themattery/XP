package br.edu.ifpb.pweb2.xp.exception;

public class ValidationException extends RuntimeException {

    private final String referer;

    public ValidationException(String message) {
        this(message, null);
    }

    public ValidationException(String message, String referer) {
        super(message);
        this.referer = referer;
    }

    public String getReferer() {
        return referer;
    }
}
