package org.hertsstack.brokerlocal;


import org.hertsstack.broker.BrokerType;
import org.hertsstack.broker.ReactiveBroker;
import org.hertsstack.broker.ReactiveConsumer;
import org.hertsstack.broker.ReactiveProducer;

import java.util.UUID;

/**
 * Concurrent local broker for broadcast load balancing.
 * This class recommends single server structure.
 *
 * @author Herts Contributer
 */
public class ConcurrentLocalBroker implements ReactiveBroker {

    private static ReactiveBroker broker;
    private final ReactiveProducer producer;
    private final ReactiveConsumer consumer;

    private ConcurrentLocalBroker(ReactiveProducer producer, ReactiveConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public static ReactiveBroker getInstance() {
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
    public ReactiveProducer getHertsMessageProducer() {
        return this.producer;
    }

    @Override
    public ReactiveConsumer getHertsMessageConsumer() {
        return this.consumer;
    }

    @Override
    public BrokerType getBrokerType() {
        return BrokerType.Local;
    }

    @Override
    public void closeBroker() {
    }
}
