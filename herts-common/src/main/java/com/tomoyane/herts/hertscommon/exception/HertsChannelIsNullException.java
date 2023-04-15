package com.tomoyane.herts.hertscommon.exception;

/**
 * Herts channel null exception class.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsChannelIsNullException extends RuntimeException {
    public HertsChannelIsNullException() {
        super();
    }

    public HertsChannelIsNullException(String msg) {
        super(msg);
    }

    public HertsChannelIsNullException(Throwable cause) {
        super(cause);
    }

    public HertsChannelIsNullException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
