package org.hertsstack.brokerredis;

/**
 * AlreadyClosedException
 * Already closed redis connection when create connection pool
 *
 * @author Herts Contributer
 */
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
