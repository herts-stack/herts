package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError417 extends HertsHttpErrorException {
    public HertsHttpError417() {
        super(StatusCode.Status417);
    }

    public HertsHttpError417(String msg) {
        super(StatusCode.Status417, msg);
    }

    public HertsHttpError417(Throwable cause) {
        super(StatusCode.Status417, cause);
    }

    public HertsHttpError417(String msg, Throwable cause) {
        super(StatusCode.Status417, msg, cause);
    }
}
