package org.herts.common.exception;

/**
 * Herts message failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsMessageException extends RuntimeException {
    public HertsMessageException() {
        super();
    }

    public HertsMessageException(String msg) {
        super(msg);
    }

    public HertsMessageException(Throwable cause) {
        super(cause);
    }

    public HertsMessageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
