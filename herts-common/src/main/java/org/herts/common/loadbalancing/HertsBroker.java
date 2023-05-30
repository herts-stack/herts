package org.herts.common.loadbalancing;

/**
 * Herts message broker for load balancing grpc connection
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsBroker {

    /**
     * Get herts producer for load balancing.
     *
     * @return HertsMessageProducer
     */
    HertsProducer getHertsMessageProducer();

    /**
     * Get herts consumer for load balancing.
     *
     * @return HertsMessageConsumer
     */
    HertsConsumer getHertsMessageConsumer();
}
