package org.herts.common.exception;

/**
 * Herts core server build failure exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsCoreBuildException extends RuntimeException {
    public HertsCoreBuildException() {
        super();
    }

    public HertsCoreBuildException(String msg) {
        super(msg);
    }

    public HertsCoreBuildException(Throwable cause) {
        super(cause);
    }

    public HertsCoreBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
