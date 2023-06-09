package org.herts.http;

import org.herts.serializer.MessageSerializeType;
import org.herts.serializer.MessageSerializer;
import org.herts.metrics.HertsMetricsServer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HertsHttpCallerBaseTest {

    class TestClass {
        public void test01() {}
        public boolean test02() { return false; }
        public String test03() { return "test02"; }
        public int test04() { return 1; }
    }

    @Test
    public void call_WithVoid() throws Exception {
        TestClass test = new TestClass();
        Class<?> c = Class.forName(test.getClass().getName());
        StubRequest req = new StubRequest();
        StubResponse res = new StubResponse();
        HertsMetricsServer metricsServer = new HertsMetricsServer(null);
        MessageSerializer serializer = new MessageSerializer(MessageSerializeType.Json);
        CallerConstructor callerConstructor = new CallerConstructor(c, req, res, metricsServer, serializer);
        HertsHttpCallerBase caller = new HertsHttpCallerBase(test, metricsServer, serializer, callerConstructor.getParameters());

        for (Method m : callerConstructor.getMethods()) {
            caller.call(m, req, res);
        }
    }
}
