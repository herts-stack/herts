package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError422 extends HertsHttpErrorException {
    public HertsHttpError422() {
        super(StatusCode.Status422);
    }

    public HertsHttpError422(String msg) {
        super(StatusCode.Status422, msg);
    }

    public HertsHttpError422(Throwable cause) {
        super(StatusCode.Status422, cause);
    }

    public HertsHttpError422(String msg, Throwable cause) {
        super(StatusCode.Status422, msg, cause);
    }
}
