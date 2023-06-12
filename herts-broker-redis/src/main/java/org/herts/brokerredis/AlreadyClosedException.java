package org.herts.brokerredis;

public class AlreadyClosedException extends RuntimeException {
    public AlreadyClosedException() {
        super();
    }

    public AlreadyClosedException(String msg) {
        super(msg);
    }

    public AlreadyClosedException(Throwable cause) {
        super(cause);
    }

    public AlreadyClosedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
