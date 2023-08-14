package org.hertsstack.core.exception;

/**
 * Herts component not support parameter type exception class.
 *
 * @author Herts Contributer
 */
public class NotSupportParameterTypeException extends RuntimeException {
    public NotSupportParameterTypeException() {
        super();
    }

    public NotSupportParameterTypeException(String msg) {
        super(msg);
    }

    public NotSupportParameterTypeException(Throwable cause) {
        super(cause);
    }

    public NotSupportParameterTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
