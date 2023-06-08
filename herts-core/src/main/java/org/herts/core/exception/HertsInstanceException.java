package org.herts.core.exception;

/**
 * Herts failure instance exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsInstanceException extends RuntimeException {
    public HertsInstanceException() {
        super();
    }

    public HertsInstanceException(String msg) {
        super(msg);
    }

    public HertsInstanceException(Throwable cause) {
        super(cause);
    }

    public HertsInstanceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
