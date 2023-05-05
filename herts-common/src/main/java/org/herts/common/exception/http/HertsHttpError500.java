package org.herts.common.exception.http;

/**
 * Herts http error status 3XX
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError500 extends HertsHttpErrorException {
    public HertsHttpError500() {
        super(StatusCode.Status500);
    }

    public HertsHttpError500(String msg) {
        super(StatusCode.Status500, msg);
    }

    public HertsHttpError500(Throwable cause) {
        super(StatusCode.Status500, cause);
    }

    public HertsHttpError500(String msg, Throwable cause) {
        super(StatusCode.Status500, msg, cause);
    }
}
