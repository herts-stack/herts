package org.hertsstack.broker;

/**
 * Herts message producer
 *
 * @author Herts Contributer
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
