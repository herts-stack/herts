package org.herts.common.exception.http;

/**
 * Herts http error
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpError402 extends HertsHttpErrorException {
    public HertsHttpError402() {
        super(StatusCode.Status402);
    }

    public HertsHttpError402(String msg) {
        super(StatusCode.Status402, msg);
    }

    public HertsHttpError402(Throwable cause) {
        super(StatusCode.Status402, cause);
    }

    public HertsHttpError402(String msg, Throwable cause) {
        super(StatusCode.Status402, msg, cause);
    }
}
