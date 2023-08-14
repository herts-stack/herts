package org.hertsstack.core.exception;

/**
 * Herts http client failure exception class.
 *
 * @author Herts Contributer
 */
public class HttpClientBuildException extends RuntimeException {
    public HttpClientBuildException() {
        super();
    }

    public HttpClientBuildException(String msg) {
        super(msg);
    }

    public HttpClientBuildException(Throwable cause) {
        super(cause);
    }

    public HttpClientBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
