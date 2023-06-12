package org.herts.brokerredis;

import org.herts.broker.BrokerType;
import org.herts.broker.ReactiveBroker;
import org.herts.broker.ReactiveConsumer;
import org.herts.broker.ReactiveProducer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.UnifiedJedis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Redis broker
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class RedisBroker implements ReactiveBroker {

    private static final String baseChannelName = "herts-reactive-group-0";
    private final ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();

    private static byte[] baseChannelNameBytes;
    private final UnifiedJedis unifiedJedis;
    private final JedisPool jedisPool;

    private RedisProducer redisProducer;
    private RedisConsumer redisConsumer;

    private RedisBroker(JedisCluster jedisCluster) {
        this.unifiedJedis = jedisCluster;
        this.jedisPool = null;

        throwIfRedisIsClosed();
    }

    private RedisBroker(JedisPool jedisPool) {
        this.unifiedJedis = null;
        this.jedisPool = jedisPool;

        throwIfRedisIsClosed();
        createProducer();
        createConsumer();
    }

    private void createProducer() {
        if (this.redisProducer == null) {
            Jedis resource = this.jedisPool.getResource();
            this.redisProducer = new RedisProducer(resource, baseChannelName);
        }
    }

    private void createConsumer() {
        if (this.redisConsumer == null) {
            Jedis resource = this.jedisPool.getResource();
            this.redisConsumer = new RedisConsumer();

            CompletableFuture.runAsync(() -> {
                try {
                    resource.subscribe(new MessagePubSub(redisConsumer), baseChannelNameBytes);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // TODO: Resubscribe processing
                }
            }, this.consumerExecutor);
        }
    }

    /**
     * Create ReactiveBroker with JedisCluster
     *
     * @param jedisCluster JedisCluster
     * @return ReactiveBroker
     */
    @Deprecated
    private static ReactiveBroker create(JedisCluster jedisCluster) {
        return new RedisBroker(jedisCluster);
    }

    /**
     * Create ReactiveBroker with JedisPool
     * If you want use TLS connection, please check jedis document.
     * See: <a href="https://redis.io/docs/clients/java/">https://redis.io/docs/clients/java/</a>
     *
     * @param jedisPool JedisPool
     * @return ReactiveBroker
     */
    public static ReactiveBroker create(JedisPool jedisPool) {
        baseChannelNameBytes = baseChannelName.getBytes();
        return new RedisBroker(jedisPool);
    }

    @Override
    public ReactiveProducer getHertsMessageProducer() {
        return this.redisProducer;
    }

    @Override
    public ReactiveConsumer getHertsMessageConsumer() {
        return this.redisConsumer;
    }

    @Override
    public BrokerType getBrokerType() {
        return BrokerType.Redis;
    }

    @Override
    public void closeBroker() {
        try {
            this.consumerExecutor.shutdown();
        } catch (Exception ignore) {
        }
        if (this.jedisPool != null) {
            this.jedisPool.close();
        }
    }

    private void throwIfRedisIsClosed() {
        if (isRedisClosed()) {
            throw new AlreadyClosedException("Redis connection pool is null or already closed");
        }
    }

    private boolean isRedisClosed() {
        return this.jedisPool == null || !this.jedisPool.getResource().isConnected();
    }
}
