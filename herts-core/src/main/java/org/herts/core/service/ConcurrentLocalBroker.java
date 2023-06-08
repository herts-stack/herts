package org.herts.core.service;

import java.util.UUID;

/**
 * Concurrent local broker for broadcast load balancing.
 * This class recommends single server structure.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class ConcurrentLocalBroker implements HertsReactiveBroker {

    private static HertsReactiveBroker broker;
    private final HertsReactiveProducer producer;
    private final HertsReactiveConsumer consumer;

    private ConcurrentLocalBroker(HertsReactiveProducer producer, HertsReactiveConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public static HertsReactiveBroker getInstance() {
        if (broker != null) {
            return broker;
        }
        String producerName = "producer-" + UUID.randomUUID();
        String consumerName = "consumer-" + UUID.randomUUID();
        broker = new ConcurrentLocalBroker(new ConcurrentLocalProducer(producerName), new ConcurrentLocalConsumer(consumerName));
        broker.getHertsMessageProducer().addObserver(broker.getHertsMessageConsumer());
        return broker;
    }

    @Override
    public HertsReactiveProducer getHertsMessageProducer() {
        return this.producer;
    }

    @Override
    public HertsReactiveConsumer getHertsMessageConsumer() {
        return this.consumer;
    }
}
