package org.hertsstack.core.exception;

/**
 * Herts channel null exception class.
 *
 * @author Herts Contributer
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
