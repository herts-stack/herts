package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError427 extends HertsHttpErrorException {
    public HertsHttpError427() {
        super(StatusCode.Status427);
    }

    public HertsHttpError427(String msg) {
        super(StatusCode.Status427, msg);
    }

    public HertsHttpError427(Throwable cause) {
        super(StatusCode.Status427, cause);
    }

    public HertsHttpError427(String msg, Throwable cause) {
        super(StatusCode.Status427, msg, cause);
    }
}
