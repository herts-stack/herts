package org.hertsstack.core.exception;

/**
 * Herts core server build failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class RpcServerBuildException extends RuntimeException {
    public RpcServerBuildException() {
        super();
    }

    public RpcServerBuildException(String msg) {
        super(msg);
    }

    public RpcServerBuildException(Throwable cause) {
        super(cause);
    }

    public RpcServerBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
