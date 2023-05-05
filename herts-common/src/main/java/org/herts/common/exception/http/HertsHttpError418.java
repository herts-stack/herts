package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError418 extends HertsHttpErrorException {
    public HertsHttpError418() {
        super(StatusCode.Status418);
    }

    public HertsHttpError418(String msg) {
        super(StatusCode.Status418, msg);
    }

    public HertsHttpError418(Throwable cause) {
        super(StatusCode.Status418, cause);
    }

    public HertsHttpError418(String msg, Throwable cause) {
        super(StatusCode.Status418, msg, cause);
    }
}
