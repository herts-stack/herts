package org.herts.common.loadbalancing.local;

import org.herts.common.loadbalancing.HertsBroker;
import org.herts.common.loadbalancing.HertsConsumer;
import org.herts.common.loadbalancing.HertsProducer;

import java.util.UUID;

/**
 * Concurrent local broker for broadcast load balancing.
 * This class recommends single server structure.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ConcurrentLocalBroker implements HertsBroker {

    private static HertsBroker broker;
    private final HertsProducer producer;
    private final HertsConsumer consumer;

    private ConcurrentLocalBroker(HertsProducer producer, HertsConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public static HertsBroker getInstance() {
        if (broker != null) {
            return broker;
        }
        var producerName = "producer-" + UUID.randomUUID();
        var consumerName = "consumer-" + UUID.randomUUID();
        broker = new ConcurrentLocalBroker(new ConcurrentLocalProducer(producerName), new ConcurrentLocalConsumer(consumerName));
        broker.getHertsMessageProducer().addObserver(broker.getHertsMessageConsumer());
        return broker;
    }

    @Override
    public HertsProducer getHertsMessageProducer() {
        return this.producer;
    }

    @Override
    public HertsConsumer getHertsMessageConsumer() {
        return this.consumer;
    }
}
