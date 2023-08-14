package org.hertsstack.rpc;

import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.metrics.HertsMetrics;
import org.hertsstack.metrics.HertsMetricsContext;
import org.hertsstack.metrics.HertsTimer;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Herts rpc with metrics caller.
 * HertsRpcCaller implementation
 *
 * @author Herts Contributer
 */
class HertsRpcMetricsCaller extends BaseCaller implements HertsRpcCaller {
    private final Method reflectMethod;
    private final HertsMetrics hertsMetrics;
    private final MessageSerializer hertsSerializer;
    private final Object coreObject;
    private final Object[] requests;

    public HertsRpcMetricsCaller(Method reflectMethod, HertsMetrics hertsMetrics, MessageSerializer hertsSerializer,
                                 Object coreObject, Object[] requests) {

        super(reflectMethod, hertsSerializer, coreObject, requests);
        this.reflectMethod = reflectMethod;
        this.hertsMetrics = hertsMetrics;
        this.hertsSerializer = hertsSerializer;
        this.coreObject = coreObject;
        this.requests = requests;
    }

    /**
     * Before call rpc.
     *
     * @return HertsTimer
     */
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

    /**
     * After call rpc
     *
     * @param timer HertsTimer
     */
    private void after(HertsTimer timer) {
        if (this.hertsMetrics.isLatencyEnabled()) {
            this.hertsMetrics.stopLatencyTimer(timer);
        }
    }

    @Override
    public <T> StreamObserver<T> invokeStreaming(Object obj, StreamObserver<T> responseObserver) throws InvocationTargetException, IllegalAccessException {
        HertsTimer timer = before();
        Object res = this.reflectMethod.invoke(obj, responseObserver);
        after(timer);
        return (StreamObserver<T>) res;
    }

    @Override
    public <T, K> Object invokeServerStreaming(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        HertsTimer timer = before();
        Object res;
        setMethodRequests(request);
        if (((byte[]) request).length > 0) {
            this.requests[this.requests.length - 1] = (StreamObserver<Object>) responseObserver;
            res = this.reflectMethod.invoke(this.coreObject, this.requests);
        } else {
            res = this.reflectMethod.invoke(this.coreObject, responseObserver);
        }
        after(timer);
        return res;
    }

    @Override
    public <T, K> Object invokeUnary(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        HertsTimer timer = before();
        Object res;
        setMethodRequests(request);
        if (this.requests != null && this.requests.length > 0) {
            res = call(this.requests);
        } else {
            res = call(null);
        }
        after(timer);
        return res;
    }
}
