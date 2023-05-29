package org.herts.common.loadbalancing;

/**
 * Herts message producer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsProducer {

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
    void addObserver(HertsConsumer observer);
}
