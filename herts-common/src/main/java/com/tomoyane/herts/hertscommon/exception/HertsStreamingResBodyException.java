package com.tomoyane.herts.hertscommon.exception;

public class HertsStreamingResBodyException extends RuntimeException {
    public HertsStreamingResBodyException() {
        super();
    }

    public HertsStreamingResBodyException(String msg) {
        super(msg);
    }

    public HertsStreamingResBodyException(Throwable cause) {
        super(cause);
    }

    public HertsStreamingResBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
