package org.herts.core.service;

/**
 * Herts message consumer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsReactiveConsumer {

    /**
     * Receive message.
     *
     * @param payload Byte array message
     */
    void receive(byte[] payload);
}
