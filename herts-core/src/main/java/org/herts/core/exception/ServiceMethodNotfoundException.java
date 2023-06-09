package org.herts.core.exception;

/**
 * Herts failure instance exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ServiceMethodNotfoundException extends RuntimeException {
    public ServiceMethodNotfoundException() {
        super();
    }

    public ServiceMethodNotfoundException(String msg) {
        super(msg);
    }

    public ServiceMethodNotfoundException(Throwable cause) {
        super(cause);
    }

    public ServiceMethodNotfoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
