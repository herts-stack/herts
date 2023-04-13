package com.tomoyane.herts.hertscommon.exception;

public class HertsServiceNotFoundException extends RuntimeException {
    public HertsServiceNotFoundException() {
        super();
    }

    public HertsServiceNotFoundException(String msg) {
        super(msg);
    }

    public HertsServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    public HertsServiceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
