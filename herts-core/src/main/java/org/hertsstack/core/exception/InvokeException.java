package org.hertsstack.core.exception;

/**
 * Herts invoke error exception
 *
 * @author Herts Contributer
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
