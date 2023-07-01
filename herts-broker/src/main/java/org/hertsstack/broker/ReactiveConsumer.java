package org.hertsstack.broker;

/**
 * Herts message consumer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface ReactiveConsumer {

    /**
     * Receive message.
     *
     * @param payload Byte array message
     */
    void receive(byte[] payload);
}
