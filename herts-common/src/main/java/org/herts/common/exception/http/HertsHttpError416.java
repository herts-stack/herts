package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError416 extends HertsHttpErrorException {
    public HertsHttpError416() {
        super(StatusCode.Status416);
    }

    public HertsHttpError416(String msg) {
        super(StatusCode.Status416, msg);
    }

    public HertsHttpError416(Throwable cause) {
        super(StatusCode.Status416, cause);
    }

    public HertsHttpError416(String msg, Throwable cause) {
        super(StatusCode.Status416, msg, cause);
    }
}
