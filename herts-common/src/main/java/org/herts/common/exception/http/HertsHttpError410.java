package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError410 extends HertsHttpErrorException {
    public HertsHttpError410() {
        super(StatusCode.Status410);
    }

    public HertsHttpError410(String msg) {
        super(StatusCode.Status410, msg);
    }

    public HertsHttpError410(Throwable cause) {
        super(StatusCode.Status410, cause);
    }

    public HertsHttpError410(String msg, Throwable cause) {
        super(StatusCode.Status410, msg, cause);
    }
}
