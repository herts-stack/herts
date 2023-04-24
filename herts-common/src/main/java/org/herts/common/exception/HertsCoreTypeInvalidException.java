package org.herts.common.exception;

/**
 * Herts core type invalid exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsCoreTypeInvalidException extends RuntimeException {
    public HertsCoreTypeInvalidException() {
        super();
    }

    public HertsCoreTypeInvalidException(String msg) {
        super(msg);
    }

    public HertsCoreTypeInvalidException(Throwable cause) {
        super(cause);
    }

    public HertsCoreTypeInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
