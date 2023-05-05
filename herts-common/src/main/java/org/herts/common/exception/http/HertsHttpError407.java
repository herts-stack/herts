package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError407 extends HertsHttpErrorException {
    public HertsHttpError407() {
        super(StatusCode.Status407);
    }

    public HertsHttpError407(String msg) {
        super(StatusCode.Status407, msg);
    }

    public HertsHttpError407(Throwable cause) {
        super(StatusCode.Status407, cause);
    }

    public HertsHttpError407(String msg, Throwable cause) {
        super(StatusCode.Status407, msg, cause);
    }
}
