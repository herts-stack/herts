package org.herts.common.exception;

/**
 * Herts grpc streaming invalid message failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsStreamingResBodyException extends RuntimeException {
    public HertsStreamingResBodyException() {
        super();
    }

    public HertsStreamingResBodyException(String msg) {
        super(msg);
    }

    public HertsStreamingResBodyException(Throwable cause) {
        super(cause);
    }

    public HertsStreamingResBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
