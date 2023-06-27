package herts.org.broker;

import org.herts.broker.ReactiveStreamingCache;
import org.herts.broker.ReactiveStreamingCacheImpl;
import org.herts.core.service.HertsReceiver;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReactiveStreamingCacheTest {
    private final ReactiveStreamingCache<HertsReceiver> cache = ReactiveStreamingCacheImpl.getInstance();

    @Nested
    class HertsReceiverTest {
        @Test
        public void setAndGet() {
            String testClient = "test_client";
            HertsReceiver receiver = new TestReactiveStreamingRpcReceiverImpl();
            cache.setHertsReceiver(testClient, receiver);

            HertsReceiver cachedReceiver = cache.getHertsReceiver(testClient);
            assertNotNull(cachedReceiver);
        }
    }
}