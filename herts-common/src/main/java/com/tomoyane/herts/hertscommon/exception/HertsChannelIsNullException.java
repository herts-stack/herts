package com.tomoyane.herts.hertscommon.exception;

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
