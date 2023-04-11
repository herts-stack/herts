package com.tomoyane.herts.hertscommon.exception;

public class HertsHttpBuildException extends RuntimeException {
    public HertsHttpBuildException() {
        super();
    }

    public HertsHttpBuildException(String msg) {
        super(msg);
    }

    public HertsHttpBuildException(Throwable cause) {
        super(cause);
    }

    public HertsHttpBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
