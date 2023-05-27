package org.herts.common.loadbalancing.local;

import org.herts.common.cache.ReactiveStreamingCache;
import org.herts.common.cache.ReactiveStreamingLocalCacheImpl;
import org.herts.common.loadbalancing.HertsInternalPayload;
import org.herts.common.loadbalancing.HertsMessageConsumer;
import org.herts.common.serializer.HertsSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConcurrentLocalConsumer implements HertsMessageConsumer {
    private final HertsSerializer serializer;
    private final ReactiveStreamingCache reactiveStreamingCache;
    private final String consumerName;

    public ConcurrentLocalConsumer(String consumerName) {
        this.serializer = new HertsSerializer();
        this.reactiveStreamingCache = ReactiveStreamingLocalCacheImpl.getInstance();
        this.consumerName = consumerName;
    }

    @Override
    public void receive(byte[] payload) {
        HertsInternalPayload hertsPayload;
        try {
            hertsPayload = this.serializer.deserialize(payload, HertsInternalPayload.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        var observer = this.reactiveStreamingCache.getObserver(hertsPayload.getClientId());
        if (observer == null) {
            return;
        }
        List<Object> parameters = new ArrayList<>();
        parameters.add(hertsPayload.getMethodName());

        try {
            for (var idx = 0; idx < hertsPayload.getParameters().size(); idx++) {
                var parameter = hertsPayload.getParameters().get(idx);
                var parameterType = hertsPayload.getParameterTypes().get(idx);
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
