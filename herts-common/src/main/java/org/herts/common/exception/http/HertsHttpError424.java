package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError424 extends HertsHttpErrorException {
    public HertsHttpError424() {
        super(StatusCode.Status424);
    }

    public HertsHttpError424(String msg) {
        super(StatusCode.Status424, msg);
    }

    public HertsHttpError424(Throwable cause) {
        super(StatusCode.Status424, cause);
    }

    public HertsHttpError424(String msg, Throwable cause) {
        super(StatusCode.Status424, msg, cause);
    }
}
