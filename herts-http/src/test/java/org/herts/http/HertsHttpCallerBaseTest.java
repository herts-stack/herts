package org.herts.http;

import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.metrics.server.HertsMetricsServer;
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
        var test = new TestClass();
        Class<?> c = Class.forName(test.getClass().getName());
        var req = new StubRequest();
        var res = new StubResponse();
        HertsMetricsServer metricsServer = new HertsMetricsServer(null);
        HertsSerializer serializer = new HertsSerializer(HertsSerializeType.Json);
        var callerConstructor = new CallerConstructor(c, req, res, metricsServer, serializer);
        var caller = new HertsHttpCallerBase(test, metricsServer, serializer, callerConstructor.getParameters());

        for (Method m : callerConstructor.getMethods()) {
            caller.call(m, req, res);
        }
    }
}