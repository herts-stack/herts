package com.tomoyane.herts.hertscommon.exception;

public class HertsCoreBuildException extends RuntimeException {
    public HertsCoreBuildException() {
        super();
    }

    public HertsCoreBuildException(String msg) {
        super(msg);
    }

    public HertsCoreBuildException(Throwable cause) {
        super(cause);
    }

    public HertsCoreBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
