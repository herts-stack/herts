package org.hertsstack.broker;

/**
 * Broker type
 *
 * @author Herts Contributer
 */
public enum BrokerType {
    /**
     * Default Broker.
     * This broker could not use multiple server. Only single instance mode.
     */
    Local,

    /**
     * Redis Broker.
     * This is load balancing by redis pubsub when you use Herts load balancing.
     */
    Redis
}
