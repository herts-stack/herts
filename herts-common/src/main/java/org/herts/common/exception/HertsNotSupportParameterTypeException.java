package org.herts.common.exception;

/**
 * Herts component not support parameter type exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsNotSupportParameterTypeException extends RuntimeException {
    public HertsNotSupportParameterTypeException() {
        super();
    }

    public HertsNotSupportParameterTypeException(String msg) {
        super(msg);
    }

    public HertsNotSupportParameterTypeException(Throwable cause) {
        super(cause);
    }

    public HertsNotSupportParameterTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
