package org.herts.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Wrapped JsonProcessingException
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsJsonProcessingException extends JsonProcessingException {
    public HertsJsonProcessingException(String msg) {
        super(msg);
    }

    public HertsJsonProcessingException(Throwable cause) {
        super(cause);
    }

    public HertsJsonProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
