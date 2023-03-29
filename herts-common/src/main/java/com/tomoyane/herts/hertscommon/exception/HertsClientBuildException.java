package com.tomoyane.herts.hertscommon.exception;

public class HertsClientBuildException extends RuntimeException {
    public HertsClientBuildException() {
        super();
    }

    public HertsClientBuildException(String msg) {
        super(msg);
    }

    public HertsClientBuildException(Throwable cause) {
        super(cause);
    }

    public HertsClientBuildException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
