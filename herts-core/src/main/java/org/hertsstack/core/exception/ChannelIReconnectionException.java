package org.hertsstack.core.exception;

/**
 * Herts channel reconnection failure.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ChannelIReconnectionException extends RuntimeException {
    public ChannelIReconnectionException() {
        super();
    }

    public ChannelIReconnectionException(String msg) {
        super(msg);
    }

    public ChannelIReconnectionException(Throwable cause) {
        super(cause);
    }

    public ChannelIReconnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
