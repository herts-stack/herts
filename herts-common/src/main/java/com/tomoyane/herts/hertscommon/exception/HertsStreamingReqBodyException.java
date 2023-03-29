package com.tomoyane.herts.hertscommon.exception;

public class HertsStreamingReqBodyException extends RuntimeException {
    public HertsStreamingReqBodyException() {
        super();
    }

    public HertsStreamingReqBodyException(String msg) {
        super(msg);
    }

    public HertsStreamingReqBodyException(Throwable cause) {
        super(cause);
    }

    public HertsStreamingReqBodyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
