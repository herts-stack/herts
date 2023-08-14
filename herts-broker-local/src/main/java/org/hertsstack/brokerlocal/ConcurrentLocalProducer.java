package org.hertsstack.brokerlocal;

import org.hertsstack.broker.ReactiveConsumer;
import org.hertsstack.broker.ReactiveProducer;
import org.hertsstack.serializer.MessageSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Concurrent local producer
 *
 * @author Herts Contributer
 */
class ConcurrentLocalProducer implements ReactiveProducer {
    private final List<ReactiveConsumer> observers;
    private final MessageSerializer serializer;
    private final String producerName;

    public ConcurrentLocalProducer(String producerName) {
        this.observers = new ArrayList<>();
        this.serializer = new MessageSerializer();
        this.producerName = producerName;
    }

    @Override
    public void produce(byte[] payload) {
        notifyObservers(payload);
    }

    @Override
    public void addObserver(ReactiveConsumer observer) {
        this.observers.add(observer);
    }

    private void notifyObservers(byte[] payload) {
        for (ReactiveConsumer observer : this.observers) {
            observer.receive(payload);
        }
    }
}
