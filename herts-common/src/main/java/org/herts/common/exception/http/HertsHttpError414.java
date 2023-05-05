package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError414 extends HertsHttpErrorException {
    public HertsHttpError414() {
        super(StatusCode.Status414);
    }

    public HertsHttpError414(String msg) {
        super(StatusCode.Status414, msg);
    }

    public HertsHttpError414(Throwable cause) {
        super(StatusCode.Status414, cause);
    }

    public HertsHttpError414(String msg, Throwable cause) {
        super(StatusCode.Status414, msg, cause);
    }
}
