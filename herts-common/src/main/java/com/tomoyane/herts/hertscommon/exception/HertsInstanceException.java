package com.tomoyane.herts.hertscommon.exception;

public class HertsInstanceException extends RuntimeException {
    public HertsInstanceException() {
        super();
    }

    public HertsInstanceException(String msg) {
        super(msg);
    }

    public HertsInstanceException(Throwable cause) {
        super(cause);
    }

    public HertsInstanceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
