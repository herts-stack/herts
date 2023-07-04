package org.hertsstack.core.exception;

/**
 * Herts component invalid message exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException() {
        super();
    }

    public InvalidMessageException(String msg) {
        super(msg);
    }

    public InvalidMessageException(Throwable cause) {
        super(cause);
    }

    public InvalidMessageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
