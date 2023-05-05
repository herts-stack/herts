package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError411 extends HertsHttpErrorException {
    public HertsHttpError411() {
        super(StatusCode.Status411);
    }

    public HertsHttpError411(String msg) {
        super(StatusCode.Status411, msg);
    }

    public HertsHttpError411(Throwable cause) {
        super(StatusCode.Status411, cause);
    }

    public HertsHttpError411(String msg, Throwable cause) {
        super(StatusCode.Status411, msg, cause);
    }
}
