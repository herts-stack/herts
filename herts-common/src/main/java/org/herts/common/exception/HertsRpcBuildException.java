package org.herts.common.exception;

/**
 * Herts core server build failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsRpcBuildException extends RuntimeException {
    public HertsRpcBuildException() {
        super();
    }

    public HertsRpcBuildException(String msg) {
        super(msg);
    }

    public HertsRpcBuildException(Throwable cause) {
        super(cause);
    }

    public HertsRpcBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
