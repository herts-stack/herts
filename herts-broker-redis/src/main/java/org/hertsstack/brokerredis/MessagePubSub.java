package org.hertsstack.brokerredis;

import redis.clients.jedis.BinaryJedisPubSub;

/**
 * MessagePubSub
 * BinaryJedisPubSub implementation
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class MessagePubSub extends BinaryJedisPubSub {
    private final RedisConsumer redisConsumer;

    public MessagePubSub(RedisConsumer redisConsumer) {
        this.redisConsumer = redisConsumer;
    }

    @Override
    public void onMessage(byte[] channel, byte[] message) {
        this.redisConsumer.receive(message);
    }

    @Override
    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
    }

    @Override
    public void onSubscribe(byte[] channel, int subscribedChannels) {
    }

    @Override
    public void onUnsubscribe(byte[] channel, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
    }

    @Override
    public void onPSubscribe(byte[] pattern, int subscribedChannels) {
    }

    @Override
    public void onPong(byte[] pattern) {
    }
}
