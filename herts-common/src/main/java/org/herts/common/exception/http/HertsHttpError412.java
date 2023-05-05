package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError412 extends HertsHttpErrorException {
    public HertsHttpError412() {
        super(StatusCode.Status412);
    }

    public HertsHttpError412(String msg) {
        super(StatusCode.Status412, msg);
    }

    public HertsHttpError412(Throwable cause) {
        super(StatusCode.Status412, cause);
    }

    public HertsHttpError412(String msg, Throwable cause) {
        super(StatusCode.Status412, msg, cause);
    }
}
