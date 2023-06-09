package org.herts.core.exception;

/**
 * Herts invoke error exception
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class InvokeException extends RuntimeException {
    public InvokeException() {
        super();
    }

    public InvokeException(String msg) {
        super(msg);
    }

    public InvokeException(Throwable cause) {
        super(cause);
    }

    public InvokeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
