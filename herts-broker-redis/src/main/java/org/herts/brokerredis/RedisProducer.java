package org.herts.brokerredis;

import org.herts.broker.ReactiveConsumer;
import org.herts.broker.ReactiveProducer;
import redis.clients.jedis.Jedis;

/**
 * Redis producer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class RedisProducer implements ReactiveProducer {
    private final Jedis publisherJedis;
    private final byte[] channel;

    public RedisProducer(Jedis publisherJedis, String channel) {
        this.publisherJedis = publisherJedis;
        this.channel = channel.getBytes();
    }

    @Override
    public void produce(byte[] payload) {
        try {
            this.publisherJedis.publish(this.channel, payload);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addObserver(ReactiveConsumer observer) {
    }
}
