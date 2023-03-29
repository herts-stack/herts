package com.tomoyane.herts.hertscommon.exception;

public class HertsMessageException extends RuntimeException {
    public HertsMessageException() {
        super();
    }

    public HertsMessageException(String msg) {
        super(msg);
    }

    public HertsMessageException(Throwable cause) {
        super(cause);
    }

    public HertsMessageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
