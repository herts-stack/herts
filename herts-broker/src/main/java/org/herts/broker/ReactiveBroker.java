package org.herts.broker;

/**
 * Herts message broker for load balancing grpc connection
 *
 * @author Herts Contributer
 * @version 1.0.0
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
