package org.herts.common.exception.http;

/**
 * Herts http error status 400
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError400 extends HertsHttpErrorException {
    public HertsHttpError400() {
        super(StatusCode.Status400);
    }

    public HertsHttpError400(String msg) {
        super(StatusCode.Status400, msg);
    }

    public HertsHttpError400(Throwable cause) {
        super(StatusCode.Status400, cause);
    }

    public HertsHttpError400(String msg, Throwable cause) {
        super(StatusCode.Status400, msg, cause);
    }
}
