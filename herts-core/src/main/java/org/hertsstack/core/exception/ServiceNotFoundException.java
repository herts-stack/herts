package org.hertsstack.core.exception;

/**
 * Herts service is not found on registered service exception class.
 *
 * @author Herts Contributer
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
