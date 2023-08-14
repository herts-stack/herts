package org.hertsstack.broker;

/**
 * Herts message broker for load balancing grpc connection
 *
 * @author Herts Contributer
 */
public interface ReactiveBroker {

    /**
     * Get herts producer for load balancing.
     *
     * @return HertsMessageProducer
     */
    ReactiveProducer getHertsMessageProducer();

    /**
     * Get herts consumer for load balancing.
     *
     * @return HertsMessageConsumer
     */
    ReactiveConsumer getHertsMessageConsumer();

    /**
     * Get broker type
     *
     * @return BrokerType
     */
    BrokerType getBrokerType();

    /**
     * Close broker.
     */
    void closeBroker();
}
