package it.unical.asde.pr78.exception;

abstract class BaseException extends RuntimeException {
    protected String message;

    public BaseException(String message) {
        super(message);
        this.message = message;
    }
}
