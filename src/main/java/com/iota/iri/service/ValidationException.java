package com.iota.iri.service;

/**
 * Validation Exception.
 */
public class ValidationException extends Exception {

    /**
     * Initializes a new instance of the ValidationException.
     */
    public ValidationException() {
        super("Invalid parameters are passed");
    }

    /**
     * Initializes a new instance of the ValidationException with the specified detail message.
     */
    public ValidationException(String msg) {
        super(msg);
    }
}
