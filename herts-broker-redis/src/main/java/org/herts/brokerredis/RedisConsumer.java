package org.herts.brokerredis;

import io.grpc.stub.StreamObserver;
import org.herts.broker.InternalReactivePayload;
import org.herts.broker.ReactiveConsumer;
import org.herts.broker.ReactiveStreamingCache;
import org.herts.broker.ReactiveStreamingCacheImpl;
import org.herts.serializer.MessageSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedisConsumer implements ReactiveConsumer {
    private final MessageSerializer serializer;
    private final ReactiveStreamingCache reactiveStreamingCache;
    private final String consumerId = UUID.randomUUID().toString();

    public RedisConsumer() {
        this.serializer = new MessageSerializer();
        this.reactiveStreamingCache = ReactiveStreamingCacheImpl.getInstance();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void receive(byte[] payload) {
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
