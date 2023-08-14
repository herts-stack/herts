package org.hertsstack.core.exception;

/**
 * Herts gateway server failure exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class GatewayServerBuildException extends RuntimeException {
    public GatewayServerBuildException() {
        super();
    }

    public GatewayServerBuildException(String msg) {
        super(msg);
    }

    public GatewayServerBuildException(Throwable cause) {
        super(cause);
    }

    public GatewayServerBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
