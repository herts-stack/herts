package org.hertsstack.core.exception;

/**
 * Herts core client build failure exception class.
 *
 * @author Herts Contributer
 */
public class RpcClientBuildException extends RuntimeException {
    public RpcClientBuildException() {
        super();
    }

    public RpcClientBuildException(String msg) {
        super(msg);
    }

    public RpcClientBuildException(Throwable cause) {
        super(cause);
    }

    public RpcClientBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
