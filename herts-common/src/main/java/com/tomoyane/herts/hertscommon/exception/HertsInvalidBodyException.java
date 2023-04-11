package com.tomoyane.herts.hertscommon.exception;

public class HertsInvalidBodyException extends RuntimeException {
    public HertsInvalidBodyException() {
        super();
    }

    public HertsInvalidBodyException(String msg) {
        super(msg);
    }

    public HertsInvalidBodyException(Throwable cause) {
        super(cause);
    }

    public HertsInvalidBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
