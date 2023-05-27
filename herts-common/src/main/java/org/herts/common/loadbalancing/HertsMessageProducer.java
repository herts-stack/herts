package org.herts.common.loadbalancing;

public interface HertsMessageProducer {
    void produce(byte[] payload);
    void addObserver(MessageObserver observer);
}
