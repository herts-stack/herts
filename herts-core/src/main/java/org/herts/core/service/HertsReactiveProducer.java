package org.herts.core.service;

/**
 * Herts message producer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsReactiveProducer {

    /**
     * Produce message.
     *
     * @param payload Byte array message
     */
    void produce(byte[] payload);

    /**
     * Add observer.
     *
     * @param observer HertsConsumer
     */
    void addObserver(HertsReactiveConsumer observer);
}
