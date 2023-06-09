package org.herts.core.exception;

/**
 * Herts channel null exception class.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ChannelIsNullException extends RuntimeException {
    public ChannelIsNullException() {
        super();
    }

    public ChannelIsNullException(String msg) {
        super(msg);
    }

    public ChannelIsNullException(Throwable cause) {
        super(cause);
    }

    public ChannelIsNullException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
