package org.herts.rpc.validator;

import org.herts.common.service.HertsService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HertsRpcValidatorTest {

    @Nested
    class isAllReturnVoid {
        @Test
        public void ok() {
            List<HertsService> hertsServices = Collections.singletonList(new TestServerStreamingService());
            var res = HertsRpcValidator.isAllReturnVoid(hertsServices);
            assertTrue(res);
        }

        @Test
        public void invalid() {
            List<HertsService> hertsServices = Arrays.asList(
                    new TestServerStreamingService(),
                    new TestServerStreamingServiceInvalid());

            var res = HertsRpcValidator.isAllReturnVoid(hertsServices);
            assertFalse(res);
        }
    }

    @Nested
    class isAllReturnStreamObserver {
        @Test
        public void ok() {
            List<HertsService> hertsServices = Collections.singletonList(new TestClientStreamingService());
            var res = HertsRpcValidator.isAllReturnStreamObserver(hertsServices);
            assertTrue(res);
        }

        @Test
        public void invalid() {
            List<HertsService> hertsServices = Arrays.asList(
                    new TestClientStreamingService(),
                    new TestClientStreamingServiceInvalid());

            var res = HertsRpcValidator.isAllReturnStreamObserver(hertsServices);
            assertFalse(res);
        }

    }

}
