package org.herts.common.exception;

/**
 * Herts core client build failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsRpcClientBuildException extends RuntimeException {
    public HertsRpcClientBuildException() {
        super();
    }

    public HertsRpcClientBuildException(String msg) {
        super(msg);
    }

    public HertsRpcClientBuildException(Throwable cause) {
        super(cause);
    }

    public HertsRpcClientBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
