package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError403 extends HertsHttpErrorException {
    public HertsHttpError403() {
        super(StatusCode.Status403);
    }

    public HertsHttpError403(String msg) {
        super(StatusCode.Status403, msg);
    }

    public HertsHttpError403(Throwable cause) {
        super(StatusCode.Status403, cause);
    }

    public HertsHttpError403(String msg, Throwable cause) {
        super(StatusCode.Status403, msg, cause);
    }
}
