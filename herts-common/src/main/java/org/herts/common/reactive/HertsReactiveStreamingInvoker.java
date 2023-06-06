package org.herts.common.reactive;

import org.herts.common.exception.HertsJsonProcessingException;
import org.herts.common.modelx.HertsReactivePayload;
import org.herts.common.loadbalancing.HertsBroker;
import org.herts.common.serializer.HertsSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Herts reactive invocation handler for server side
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsReactiveStreamingInvoker implements InvocationHandler {
    private final HertsBroker hertsMessageBroker;
    private final HertsSerializer hertsSerializer;
    private String clientId;

    public HertsReactiveStreamingInvoker(HertsBroker hertsMessageBroker, String clientId) {
        this.hertsMessageBroker = hertsMessageBroker;
        this.hertsSerializer = new HertsSerializer();
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

        HertsReactivePayload hertsPayload = new HertsReactivePayload();
        hertsPayload.setClientId(this.clientId);
        hertsPayload.setMethodName(method.getName());
        hertsPayload.setParameters(parameters);
        hertsPayload.setParameterTypes(parameterTypes);

        try {
            this.hertsMessageBroker.getHertsMessageProducer().produce(this.hertsSerializer.serialize(hertsPayload));
        } catch (HertsJsonProcessingException ex) {
            ex.printStackTrace();
        }
        return proxy;
    }
}
