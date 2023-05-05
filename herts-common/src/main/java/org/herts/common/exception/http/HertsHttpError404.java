package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError404 extends HertsHttpErrorException {
    public HertsHttpError404() {
        super(StatusCode.Status404);
    }

    public HertsHttpError404(String msg) {
        super(StatusCode.Status404, msg);
    }

    public HertsHttpError404(Throwable cause) {
        super(StatusCode.Status404, cause);
    }

    public HertsHttpError404(String msg, Throwable cause) {
        super(StatusCode.Status404, msg, cause);
    }
}
