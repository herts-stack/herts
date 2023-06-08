package org.herts.core.exception;

/**
 * Herts grpc streaming invalid message failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsStreamingReqBodyException extends RuntimeException {
    public HertsStreamingReqBodyException() {
        super();
    }

    public HertsStreamingReqBodyException(String msg) {
        super(msg);
    }

    public HertsStreamingReqBodyException(Throwable cause) {
        super(cause);
    }

    public HertsStreamingReqBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
