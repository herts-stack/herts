package org.hertsstack.core.exception;

/**
 * Herts stream error exception
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class StreamParsingException extends RuntimeException {
    public StreamParsingException(String msg) {
        super(msg);
    }

    public StreamParsingException(Throwable cause) {
        super(cause);
    }

    public StreamParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
