package org.herts.common.exception;

/**
 * Herts core client build failure exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsCoreClientBuildException extends RuntimeException {
    public HertsCoreClientBuildException() {
        super();
    }

    public HertsCoreClientBuildException(String msg) {
        super(msg);
    }

    public HertsCoreClientBuildException(Throwable cause) {
        super(cause);
    }

    public HertsCoreClientBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
