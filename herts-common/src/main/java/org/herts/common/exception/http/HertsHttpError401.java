package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError401 extends HertsHttpErrorException {
    public HertsHttpError401() {
        super(StatusCode.Status401);
    }

    public HertsHttpError401(String msg) {
        super(StatusCode.Status401, msg);
    }

    public HertsHttpError401(Throwable cause) {
        super(StatusCode.Status401, cause);
    }

    public HertsHttpError401(String msg, Throwable cause) {
        super(StatusCode.Status401, msg, cause);
    }
}
