package org.herts.common.loadbalancing;

/**
 * Herts message broker for load balancing grpc connection
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsMessageBroker {
    HertsMessageProducer getHertsMessageProducer();
    HertsMessageConsumer getHertsMessageConsumer();
}
