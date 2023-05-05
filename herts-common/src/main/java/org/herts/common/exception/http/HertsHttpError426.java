package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError426 extends HertsHttpErrorException {
    public HertsHttpError426() {
        super(StatusCode.Status426);
    }

    public HertsHttpError426(String msg) {
        super(StatusCode.Status426, msg);
    }

    public HertsHttpError426(Throwable cause) {
        super(StatusCode.Status426, cause);
    }

    public HertsHttpError426(String msg, Throwable cause) {
        super(StatusCode.Status426, msg, cause);
    }
}
