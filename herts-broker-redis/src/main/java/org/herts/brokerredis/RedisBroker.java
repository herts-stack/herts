package org.herts.brokerredis;

import org.herts.broker.ReactiveBroker;
import org.herts.broker.ReactiveConsumer;
import org.herts.broker.ReactiveProducer;

public class RedisBroker implements ReactiveBroker {
    @Override
    public ReactiveProducer getHertsMessageProducer() {
        return null;
    }

    @Override
    public ReactiveConsumer getHertsMessageConsumer() {
        return null;
    }
}
