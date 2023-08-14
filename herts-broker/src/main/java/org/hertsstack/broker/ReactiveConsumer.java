package org.hertsstack.broker;

/**
 * Herts message consumer
 *
 * @author Herts Contributer
 */
public interface ReactiveConsumer {

    /**
     * Receive message.
     *
     * @param payload Byte array message
     */
    void receive(byte[] payload);
}
