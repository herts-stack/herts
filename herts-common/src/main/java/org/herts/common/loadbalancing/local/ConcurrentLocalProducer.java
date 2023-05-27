package org.herts.common.loadbalancing.local;

import org.herts.common.loadbalancing.HertsMessageProducer;
import org.herts.common.loadbalancing.MessageObserver;
import org.herts.common.serializer.HertsSerializer;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentLocalProducer implements HertsMessageProducer {
    private final List<MessageObserver> observers;
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
    public void addObserver(MessageObserver observer) {
        this.observers.add(observer);
    }

    private void notifyObservers(byte[] payload) {
        for (MessageObserver observer : this.observers) {
            observer.receive(payload);
        }
    }
}
