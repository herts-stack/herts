package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError415 extends HertsHttpErrorException {
    public HertsHttpError415() {
        super(StatusCode.Status415);
    }

    public HertsHttpError415(String msg) {
        super(StatusCode.Status415, msg);
    }

    public HertsHttpError415(Throwable cause) {
        super(StatusCode.Status415, cause);
    }

    public HertsHttpError415(String msg, Throwable cause) {
        super(StatusCode.Status415, msg, cause);
    }
}
