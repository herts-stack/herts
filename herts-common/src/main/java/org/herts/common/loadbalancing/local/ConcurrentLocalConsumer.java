package org.herts.common.loadbalancing.local;

import io.grpc.stub.StreamObserver;
import org.herts.common.reactive.ReactiveStreamingCache;
import org.herts.common.reactive.ReactiveStreamingCacheImpl;
import org.herts.common.modelx.HertsReactivePayload;
import org.herts.common.loadbalancing.HertsConsumer;
import org.herts.common.serializer.HertsSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Concurrent local consumer
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ConcurrentLocalConsumer implements HertsConsumer {
    private final HertsSerializer serializer;
    private final ReactiveStreamingCache reactiveStreamingCache;
    private final String consumerName;

    public ConcurrentLocalConsumer(String consumerName) {
        this.serializer = new HertsSerializer();
        this.reactiveStreamingCache = ReactiveStreamingCacheImpl.getInstance();
        this.consumerName = consumerName;
    }

    @Override
    public synchronized void receive(byte[] payload) {
        HertsReactivePayload hertsPayload;
        try {
            hertsPayload = this.serializer.deserialize(payload, HertsReactivePayload.class);
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
