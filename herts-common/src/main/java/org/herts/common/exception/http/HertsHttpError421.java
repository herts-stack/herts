package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError421 extends HertsHttpErrorException {
    public HertsHttpError421() {
        super(StatusCode.Status421);
    }

    public HertsHttpError421(String msg) {
        super(StatusCode.Status421, msg);
    }

    public HertsHttpError421(Throwable cause) {
        super(StatusCode.Status421, cause);
    }

    public HertsHttpError421(String msg, Throwable cause) {
        super(StatusCode.Status421, msg, cause);
    }
}
