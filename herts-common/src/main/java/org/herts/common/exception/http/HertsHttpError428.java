package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError428 extends HertsHttpErrorException {
    public HertsHttpError428() {
        super(StatusCode.Status428);
    }

    public HertsHttpError428(String msg) {
        super(StatusCode.Status428, msg);
    }

    public HertsHttpError428(Throwable cause) {
        super(StatusCode.Status428, cause);
    }

    public HertsHttpError428(String msg, Throwable cause) {
        super(StatusCode.Status428, msg, cause);
    }
}
