package org.herts.core.service;

import org.herts.core.exception.MessageJsonParsingException;
import org.herts.core.modelx.InternalReactivePayload;
import org.herts.core.serializer.MessageSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Herts reactive invocation handler for server side
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsReactiveStreamingInvoker implements InvocationHandler {
    private final HertsReactiveBroker hertsMessageBroker;
    private final MessageSerializer hertsSerializer;
    private String clientId;

    public HertsReactiveStreamingInvoker(HertsReactiveBroker hertsMessageBroker, String clientId) {
        this.hertsMessageBroker = hertsMessageBroker;
        this.hertsSerializer = new MessageSerializer();
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
            parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        if (this.clientId == null || this.clientId.isEmpty()) {
            return proxy;
        }

        InternalReactivePayload hertsPayload = new InternalReactivePayload();
        hertsPayload.setClientId(this.clientId);
        hertsPayload.setMethodName(method.getName());
        hertsPayload.setParameters(parameters);
        hertsPayload.setParameterTypes(parameterTypes);

        try {
            this.hertsMessageBroker.getHertsMessageProducer().produce(this.hertsSerializer.serialize(hertsPayload));
        } catch (MessageJsonParsingException ex) {
            ex.printStackTrace();
        }
        return proxy;
    }
}
