package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError423 extends HertsHttpErrorException {
    public HertsHttpError423() {
        super(StatusCode.Status423);
    }

    public HertsHttpError423(String msg) {
        super(StatusCode.Status423, msg);
    }

    public HertsHttpError423(Throwable cause) {
        super(StatusCode.Status423, cause);
    }

    public HertsHttpError423(String msg, Throwable cause) {
        super(StatusCode.Status423, msg, cause);
    }
}
