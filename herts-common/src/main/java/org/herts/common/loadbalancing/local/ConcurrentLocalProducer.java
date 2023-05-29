package org.herts.common.loadbalancing.local;

import org.herts.common.loadbalancing.HertsConsumer;
import org.herts.common.loadbalancing.HertsProducer;
import org.herts.common.serializer.HertsSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Concurrent local producer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ConcurrentLocalProducer implements HertsProducer {
    private final List<HertsConsumer> observers;
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
    public void addObserver(HertsConsumer observer) {
        this.observers.add(observer);
    }

    private void notifyObservers(byte[] payload) {
        for (HertsConsumer observer : this.observers) {
            observer.receive(payload);
        }
    }
}
