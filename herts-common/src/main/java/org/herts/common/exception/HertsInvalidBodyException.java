package org.herts.common.exception;

/**
 * Herts component invalid message exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsInvalidBodyException extends RuntimeException {
    public HertsInvalidBodyException() {
        super();
    }

    public HertsInvalidBodyException(String msg) {
        super(msg);
    }

    public HertsInvalidBodyException(Throwable cause) {
        super(cause);
    }

    public HertsInvalidBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
