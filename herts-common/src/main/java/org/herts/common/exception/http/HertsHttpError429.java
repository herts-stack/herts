package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError429 extends HertsHttpErrorException {
    public HertsHttpError429() {
        super(StatusCode.Status429);
    }

    public HertsHttpError429(String msg) {
        super(StatusCode.Status429, msg);
    }

    public HertsHttpError429(Throwable cause) {
        super(StatusCode.Status429, cause);
    }

    public HertsHttpError429(String msg, Throwable cause) {
        super(StatusCode.Status429, msg, cause);
    }
}
