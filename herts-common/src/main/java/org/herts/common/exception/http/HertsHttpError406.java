package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError406 extends HertsHttpErrorException {
    public HertsHttpError406() {
        super(StatusCode.Status406);
    }

    public HertsHttpError406(String msg) {
        super(StatusCode.Status406, msg);
    }

    public HertsHttpError406(Throwable cause) {
        super(StatusCode.Status406, cause);
    }

    public HertsHttpError406(String msg, Throwable cause) {
        super(StatusCode.Status406, msg, cause);
    }
}
