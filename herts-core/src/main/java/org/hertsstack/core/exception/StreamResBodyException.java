package org.hertsstack.core.exception;

/**
 * Herts grpc streaming invalid message failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class StreamResBodyException extends RuntimeException {
    public StreamResBodyException() {
        super();
    }

    public StreamResBodyException(String msg) {
        super(msg);
    }

    public StreamResBodyException(Throwable cause) {
        super(cause);
    }

    public StreamResBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
