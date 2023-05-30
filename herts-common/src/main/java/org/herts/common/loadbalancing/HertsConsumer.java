package org.herts.common.loadbalancing;

/**
 * Herts message consumer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsConsumer {

    /**
     * Receive message.
     *
     * @param payload Byte array message
     */
    void receive(byte[] payload);
}
