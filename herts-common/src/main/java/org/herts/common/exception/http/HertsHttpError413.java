package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError413 extends HertsHttpErrorException {
    public HertsHttpError413() {
        super(StatusCode.Status413);
    }

    public HertsHttpError413(String msg) {
        super(StatusCode.Status413, msg);
    }

    public HertsHttpError413(Throwable cause) {
        super(StatusCode.Status413, cause);
    }

    public HertsHttpError413(String msg, Throwable cause) {
        super(StatusCode.Status413, msg, cause);
    }
}
