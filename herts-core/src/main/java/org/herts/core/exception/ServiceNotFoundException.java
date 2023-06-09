package org.herts.core.exception;

/**
 * Herts service is not found on registered service exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException() {
        super();
    }

    public ServiceNotFoundException(String msg) {
        super(msg);
    }

    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServiceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
