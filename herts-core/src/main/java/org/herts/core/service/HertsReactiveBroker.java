package org.herts.core.service;

/**
 * Herts message broker for load balancing grpc connection
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsReactiveBroker {

    /**
     * Get herts producer for load balancing.
     *
     * @return HertsMessageProducer
     */
    HertsReactiveProducer getHertsMessageProducer();

    /**
     * Get herts consumer for load balancing.
     *
     * @return HertsMessageConsumer
     */
    HertsReactiveConsumer getHertsMessageConsumer();
}
