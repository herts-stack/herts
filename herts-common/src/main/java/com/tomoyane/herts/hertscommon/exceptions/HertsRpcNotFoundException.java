package com.tomoyane.herts.hertscommon.exceptions;

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
}
