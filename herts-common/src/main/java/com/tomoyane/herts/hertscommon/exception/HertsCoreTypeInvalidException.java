package com.tomoyane.herts.hertscommon.exception;

public class HertsCoreTypeInvalidException extends RuntimeException {
    public HertsCoreTypeInvalidException() {
        super();
    }

    public HertsCoreTypeInvalidException(String msg) {
        super(msg);
    }

    public HertsCoreTypeInvalidException(Throwable cause) {
        super(cause);
    }

    public HertsCoreTypeInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
