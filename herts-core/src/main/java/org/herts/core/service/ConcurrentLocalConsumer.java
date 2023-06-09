package org.herts.core.service;

import io.grpc.stub.StreamObserver;
import org.herts.core.modelx.InternalReactivePayload;
import org.herts.core.serializer.MessageSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Concurrent local consumer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class ConcurrentLocalConsumer implements HertsReactiveConsumer {
    private final MessageSerializer serializer;
    private final ReactiveStreamingCache reactiveStreamingCache;
    private final String consumerName;

    public ConcurrentLocalConsumer(String consumerName) {
        this.serializer = new MessageSerializer();
        this.reactiveStreamingCache = ReactiveStreamingCacheImpl.getInstance();
        this.consumerName = consumerName;
    }

    @Override
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
