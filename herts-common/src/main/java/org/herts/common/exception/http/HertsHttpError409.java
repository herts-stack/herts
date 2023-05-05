package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError409 extends HertsHttpErrorException {
    public HertsHttpError409() {
        super(StatusCode.Status409);
    }

    public HertsHttpError409(String msg) {
        super(StatusCode.Status409, msg);
    }

    public HertsHttpError409(Throwable cause) {
        super(StatusCode.Status409, cause);
    }

    public HertsHttpError409(String msg, Throwable cause) {
        super(StatusCode.Status409, msg, cause);
    }
}
