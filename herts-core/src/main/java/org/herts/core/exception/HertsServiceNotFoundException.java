package org.herts.core.exception;

/**
 * Herts service is not found on registered service exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsServiceNotFoundException extends RuntimeException {
    public HertsServiceNotFoundException() {
        super();
    }

    public HertsServiceNotFoundException(String msg) {
        super(msg);
    }

    public HertsServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    public HertsServiceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
