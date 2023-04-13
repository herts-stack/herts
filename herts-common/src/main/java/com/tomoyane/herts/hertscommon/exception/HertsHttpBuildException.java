package com.tomoyane.herts.hertscommon.exception;

/**
 * Herts http server failure exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpBuildException extends RuntimeException {
    public HertsHttpBuildException() {
        super();
    }

    public HertsHttpBuildException(String msg) {
        super(msg);
    }

    public HertsHttpBuildException(Throwable cause) {
        super(cause);
    }

    public HertsHttpBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
