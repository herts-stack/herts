package com.tomoyane.herts.hertscommon.exception;

public class HertsRpcNotFoundException extends RuntimeException {
    public HertsRpcNotFoundException() {
        super();
    }

    public HertsRpcNotFoundException(String msg) {
        super(msg);
    }

    public HertsRpcNotFoundException(Throwable cause) {
        super(cause);
    }

    public HertsRpcNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
