package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError408 extends HertsHttpErrorException {
    public HertsHttpError408() {
        super(StatusCode.Status408);
    }

    public HertsHttpError408(String msg) {
        super(StatusCode.Status408, msg);
    }

    public HertsHttpError408(Throwable cause) {
        super(StatusCode.Status408, cause);
    }

    public HertsHttpError408(String msg, Throwable cause) {
        super(StatusCode.Status408, msg, cause);
    }
}
