package org.hertsstack.brokerlocal;

import io.grpc.stub.StreamObserver;

import org.hertsstack.broker.InternalReactivePayload;
import org.hertsstack.broker.ReactiveConsumer;
import org.hertsstack.broker.ReactiveStreamingCache;
import org.hertsstack.broker.ReactiveStreamingCacheImpl;
import org.hertsstack.serializer.MessageSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Concurrent local consumer
 *
 * @author Herts Contributer
 */
class ConcurrentLocalConsumer implements ReactiveConsumer {
    private final MessageSerializer serializer;
    private final ReactiveStreamingCache reactiveStreamingCache;
    private final String consumerName;

    public ConcurrentLocalConsumer(String consumerName) {
        this.serializer = new MessageSerializer();
        this.reactiveStreamingCache = ReactiveStreamingCacheImpl.getInstance();
        this.consumerName = consumerName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void receive(byte[] payload) {
        InternalReactivePayload hertsPayload;
        try {
            hertsPayload = this.serializer.deserialize(payload, InternalReactivePayload.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        StreamObserver<Object> observer = this.reactiveStreamingCache.getObserver(hertsPayload.getClientId());
        if (observer == null) {
            return;
        }
        List<Object> parameters = new ArrayList<>();
        parameters.add(hertsPayload.getMethodName());

        try {
            for (int idx = 0; idx < hertsPayload.getParameters().size(); idx++) {
                Object parameter = hertsPayload.getParameters().get(idx);
                Class<?> parameterType = hertsPayload.getParameterTypes().get(idx);
                if (parameter != null) {
                    parameters.add(this.serializer.convert(parameter, parameterType));
                } else {
                    parameters.add(null);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        observer.onNext(parameters);
    }
}
