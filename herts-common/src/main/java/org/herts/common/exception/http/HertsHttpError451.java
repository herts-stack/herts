package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError451 extends HertsHttpErrorException {
    public HertsHttpError451() {
        super(StatusCode.Status451);
    }

    public HertsHttpError451(String msg) {
        super(StatusCode.Status451, msg);
    }

    public HertsHttpError451(Throwable cause) {
        super(StatusCode.Status451, cause);
    }

    public HertsHttpError451(String msg, Throwable cause) {
        super(StatusCode.Status451, msg, cause);
    }
}
