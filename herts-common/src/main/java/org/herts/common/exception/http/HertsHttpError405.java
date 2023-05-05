package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError405 extends HertsHttpErrorException {
    public HertsHttpError405() {
        super(StatusCode.Status405);
    }

    public HertsHttpError405(String msg) {
        super(StatusCode.Status405, msg);
    }

    public HertsHttpError405(Throwable cause) {
        super(StatusCode.Status405, cause);
    }

    public HertsHttpError405(String msg, Throwable cause) {
        super(StatusCode.Status405, msg, cause);
    }
}
