package org.herts.common.exception;

/**
 * Herts core type invalid exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsTypeInvalidException extends RuntimeException {
    public HertsTypeInvalidException() {
        super();
    }

    public HertsTypeInvalidException(String msg) {
        super(msg);
    }

    public HertsTypeInvalidException(Throwable cause) {
        super(cause);
    }

    public HertsTypeInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
