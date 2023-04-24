package org.herts.rpc.handler;

import org.herts.common.context.HertsMsg;
import org.herts.common.serializer.HertsSerializer;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.context.HertsMetricsContext;
import org.herts.metrics.context.HertsTimer;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HertsRpcMetricsCaller implements HertsRpcCaller {
    private final Method reflectMethod;
    private final HertsMetrics hertsMetrics;
    private final HertsSerializer hertsSerializer;
    private final Object coreObject;
    private final Object[] requests;

    public HertsRpcMetricsCaller(Method reflectMethod, HertsMetrics hertsMetrics, HertsSerializer hertsSerializer,
                                 Object coreObject, Object[] requests) {
        this.reflectMethod = reflectMethod;
        this.hertsMetrics = hertsMetrics;
        this.hertsSerializer = hertsSerializer;
        this.coreObject = coreObject;
        this.requests = requests;
    }

    private HertsTimer before() {
        HertsTimer timer = null;
        if (this.hertsMetrics.isLatencyEnabled()) {
            timer = this.hertsMetrics.startLatencyTimer(this.reflectMethod.getName());
        }
        if (this.hertsMetrics.isRpsEnabled()) {
            this.hertsMetrics.counter(HertsMetricsContext.Metric.Rps, this.reflectMethod.getName());
        }
        return timer;
    }

    private void after(HertsTimer timer) {
        if (this.hertsMetrics.isLatencyEnabled()) {
            this.hertsMetrics.stopLatencyTimer(timer);
        }
    }

    @Override
    public <T> StreamObserver<T> invokeStreaming(Object obj, StreamObserver<T> responseObserver) throws InvocationTargetException, IllegalAccessException {
        var timer = before();
        var res = this.reflectMethod.invoke(obj, responseObserver);
        after(timer);
        return (StreamObserver<T>) res;
    }

    @Override
    public <T, K> Object invokeServerStreaming(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        var timer = before();
        Object res;
        if (((byte[]) request).length > 0) {
            HertsMsg deserialized = this.hertsSerializer.deserialize((byte[]) request, HertsMsg.class);
            var index = 0;
            for (Object obj : deserialized.getMessageParameters()) {
                var castType = deserialized.getClassTypes()[index];
                this.requests[index] = this.hertsSerializer.convert(obj, castType);
                index++;
            }
            this.requests[this.requests.length-1] =  (StreamObserver<Object>) responseObserver;
            res = this.reflectMethod.invoke(this.coreObject, this.requests);
        } else {
            res = this.reflectMethod.invoke(this.coreObject, responseObserver);
        }
        after(timer);
        return res;
    }

    @Override
    public <T, K> Object invokeUnary(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        var timer = before();
        Object res;
        if (((byte[]) request).length > 0) {
            HertsMsg deserialized = this.hertsSerializer.deserialize((byte[]) request, HertsMsg.class);
            var index = 0;
            for (Object obj : deserialized.getMessageParameters()) {
                var castType = deserialized.getClassTypes()[index];
                this.requests[index] = this.hertsSerializer.convert(obj, castType);
                index++;
            }

            res = this.reflectMethod.invoke(this.coreObject, this.requests);
        } else {
            res = this.reflectMethod.invoke(this.coreObject);
        }
        after(timer);
        return res;
    }
}
