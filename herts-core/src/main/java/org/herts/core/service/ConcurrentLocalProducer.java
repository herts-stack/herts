package org.herts.core.service;

import org.herts.core.serializer.HertsSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Concurrent local producer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class ConcurrentLocalProducer implements HertsReactiveProducer {
    private final List<HertsReactiveConsumer> observers;
    private final HertsSerializer serializer;
    private final String producerName;

    public ConcurrentLocalProducer(String producerName) {
        this.observers = new ArrayList<>();
        this.serializer = new HertsSerializer();
        this.producerName = producerName;
    }

    @Override
    public void produce(byte[] payload) {
        notifyObservers(payload);
    }

    @Override
    public void addObserver(HertsReactiveConsumer observer) {
        this.observers.add(observer);
    }

    private void notifyObservers(byte[] payload) {
        for (HertsReactiveConsumer observer : this.observers) {
            observer.receive(payload);
        }
    }
}