package org.herts.broker;

/**
 * Herts message producer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface ReactiveProducer {

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
    void addObserver(ReactiveConsumer observer);
}
