package com.tomoyane.herts.hertscommon.exceptions;

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
}
