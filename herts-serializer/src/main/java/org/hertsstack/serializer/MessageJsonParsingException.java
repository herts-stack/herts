package org.hertsstack.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Wrapped JsonProcessingException
 *
 * @author Herts Contributer
 */
public class MessageJsonParsingException extends JsonProcessingException {
    public MessageJsonParsingException(String msg) {
        super(msg);
    }

    public MessageJsonParsingException(Throwable cause) {
        super(cause);
    }

    public MessageJsonParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
