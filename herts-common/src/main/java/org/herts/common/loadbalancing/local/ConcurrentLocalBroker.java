package org.herts.common.loadbalancing.local;

import org.herts.common.loadbalancing.HertsMessageBroker;
import org.herts.common.loadbalancing.HertsMessageConsumer;
import org.herts.common.loadbalancing.HertsMessageProducer;

import java.util.UUID;

public class ConcurrentLocalBroker implements HertsMessageBroker {

    private static HertsMessageBroker broker;
    private final HertsMessageProducer producer;
    private final HertsMessageConsumer consumer;

    private ConcurrentLocalBroker(HertsMessageProducer producer, HertsMessageConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public static HertsMessageBroker getInstance() {
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
    public HertsMessageProducer getHertsMessageProducer() {
        return this.producer;
    }

    @Override
    public HertsMessageConsumer getHertsMessageConsumer() {
        return this.consumer;
    }
}
