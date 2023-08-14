package org.hertsstack.core.exception;

/**
 * Herts grpc streaming invalid message failure exception class.
 *
 * @author Herts Contributer
 */
public class StreamBodyException extends RuntimeException {
    public StreamBodyException() {
        super();
    }

    public StreamBodyException(String msg) {
        super(msg);
    }

    public StreamBodyException(Throwable cause) {
        super(cause);
    }

    public StreamBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
