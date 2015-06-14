package org.minimalcode.convert;

public class ConversionException extends RuntimeException {

    /**
     * Constructs a new conversion exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a new conversion exception with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
