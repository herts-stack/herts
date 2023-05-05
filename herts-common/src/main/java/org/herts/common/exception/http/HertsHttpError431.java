package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError431 extends HertsHttpErrorException {
    public HertsHttpError431() {
        super(StatusCode.Status431);
    }

    public HertsHttpError431(String msg) {
        super(StatusCode.Status431, msg);
    }

    public HertsHttpError431(Throwable cause) {
        super(StatusCode.Status431, cause);
    }

    public HertsHttpError431(String msg, Throwable cause) {
        super(StatusCode.Status431, msg, cause);
    }
}
