package org.hertsstack.brokerredis;

import org.hertsstack.broker.ReactiveConsumer;
import org.hertsstack.broker.ReactiveProducer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis producer
 *
 * @author Herts Contributer
 */
public class RedisProducer implements ReactiveProducer {
    private final JedisPool jedisPool;
    private final byte[] channel;

    public RedisProducer(JedisPool jedisPool, String channel) {
        this.jedisPool = jedisPool;
        this.channel = channel.getBytes();
    }

    @Override
    public void produce(byte[] payload) {
        try {
            try (Jedis resource = jedisPool.getResource()) {
                resource.publish(this.channel, payload);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addObserver(ReactiveConsumer observer) {
    }
}
