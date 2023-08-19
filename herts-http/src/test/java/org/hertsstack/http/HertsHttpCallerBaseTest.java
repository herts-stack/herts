package org.hertsstack.http;

import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.modelx.RegisteredMethod;
import org.hertsstack.serializer.MessageSerializeType;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.metrics.HertsMetricsServer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

        ConcurrentMap<String, RegisteredMethod> registeredMethods = new ConcurrentHashMap<>();
        for (Method method : test.getClass().getDeclaredMethods()) {
            Parameter[] parameters = test.getClass().getMethod(method.getName(), method.getParameterTypes()).getParameters();
            RegisteredMethod registeredMethod = new RegisteredMethod(
                    HertsType.Http,
                    test.getClass().getSimpleName(),
                    test.getClass().getSimpleName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes()
            );
            registeredMethod.setParameters(parameters);
            registeredMethods.put(method.getName(), registeredMethod);
        }

        HertsHttpCallerBase caller = new HertsHttpCallerBase(test, metricsServer, serializer, registeredMethods);

        for (Method m : callerConstructor.getMethods()) {
            caller.call(m, req, res);
        }
    }
}
