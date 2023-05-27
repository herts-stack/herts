package org.herts.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsSystemContext;
import org.herts.common.loadbalancing.HertsInternalPayload;
import org.herts.common.loadbalancing.HertsMessageBroker;
import org.herts.common.serializer.HertsSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Herts reactive invocation handler for server side
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsReactiveStreamingInvoker implements InvocationHandler {
    private final HertsMessageBroker hertsMessageBroker;
    private final HertsSerializer hertsSerializer;
    private String clientId;

    public HertsReactiveStreamingInvoker(HertsMessageBroker hertsMessageBroker) {
        this.hertsMessageBroker = hertsMessageBroker;
        this.hertsSerializer = new HertsSerializer();
    }

    public void setTarget(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        List<Object> parameters = new ArrayList<>();
        List<Class<?>> parameterTypes = new ArrayList<>();
        if (args != null) {
            Collections.addAll(parameters, args);
        }
        if (method.getParameterTypes().length > 0) {
            parameterTypes = List.of(method.getParameterTypes());
        }

        if (this.clientId == null || this.clientId.isEmpty()) {
            return proxy;
        }

        var hertsPayload = new HertsInternalPayload();
        hertsPayload.setClientId(this.clientId);
        hertsPayload.setMethodName(method.getName());
        hertsPayload.setParameters(parameters);
        hertsPayload.setParameterTypes(parameterTypes);

        try {
            this.hertsMessageBroker.getHertsMessageProducer().produce(this.hertsSerializer.serialize(hertsPayload));
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return proxy;
    }
}
