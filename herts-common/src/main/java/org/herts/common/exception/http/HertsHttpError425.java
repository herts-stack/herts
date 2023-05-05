package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError425 extends HertsHttpErrorException {
    public HertsHttpError425() {
        super(StatusCode.Status425);
    }

    public HertsHttpError425(String msg) {
        super(StatusCode.Status425, msg);
    }

    public HertsHttpError425(Throwable cause) {
        super(StatusCode.Status425, cause);
    }

    public HertsHttpError425(String msg, Throwable cause) {
        super(StatusCode.Status425, msg, cause);
    }
}
