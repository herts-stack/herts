package com.tomoyane.herts.hertscommon.exception;

public class HertsNotSupportParameterTypeException extends RuntimeException {
    public HertsNotSupportParameterTypeException() {
        super();
    }

    public HertsNotSupportParameterTypeException(String msg) {
        super(msg);
    }

    public HertsNotSupportParameterTypeException(Throwable cause) {
        super(cause);
    }

    public HertsNotSupportParameterTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
