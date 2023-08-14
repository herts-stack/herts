package org.hertsstack.core.exception;

/**
 * Herts http server failure exception class.
 *
 * @author Herts Contributer
 */
public class HttpServerBuildException extends RuntimeException {
    public HttpServerBuildException() {
        super();
    }

    public HttpServerBuildException(String msg) {
        super(msg);
    }

    public HttpServerBuildException(Throwable cause) {
        super(cause);
    }

    public HttpServerBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
