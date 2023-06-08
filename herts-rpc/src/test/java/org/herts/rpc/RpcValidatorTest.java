package org.herts.rpc;

import org.herts.core.service.HertsService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RpcValidatorTest {

    @Nested
    class isAllReturnVoid {
        @Test
        public void ok() {
            List<HertsService> hertsServices = Collections.singletonList(new TestServerStreamingService());
            boolean res = RpcValidator.isAllReturnVoid(hertsServices);
            assertTrue(res);
        }

        @Test
        public void invalid() {
            List<HertsService> hertsServices = Arrays.asList(
                    new TestServerStreamingService(),
                    new TestServerStreamingServiceInvalid());

            boolean res = RpcValidator.isAllReturnVoid(hertsServices);
            assertFalse(res);
        }
    }

    @Nested
    class isAllReturnStreamObserver {
        @Test
        public void ok() {
            List<HertsService> hertsServices = Collections.singletonList(new TestClientStreamingService());
            boolean res = RpcValidator.isAllReturnStreamObserver(hertsServices);
            assertTrue(res);
        }

        @Test
        public void invalid() {
            List<HertsService> hertsServices = Arrays.asList(
                    new TestClientStreamingService(),
                    new TestClientStreamingServiceInvalid());

            boolean res = RpcValidator.isAllReturnStreamObserver(hertsServices);
            assertFalse(res);
        }
    }

    @Nested
    class isAllReceiverVoid {
        @Test
        public void ok() {
            List<HertsService> hertsServices = Collections.singletonList(new TestReactiveStreamingService());
            boolean res = RpcValidator.isAllReceiverVoid(hertsServices);
            assertTrue(res);
        }
    }
}
