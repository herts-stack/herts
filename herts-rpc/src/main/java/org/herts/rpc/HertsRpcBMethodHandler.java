package org.herts.rpc;

import org.herts.core.exception.HertsInstanceException;
import org.herts.core.exception.HertsMessageException;
import org.herts.core.modelx.HertsMethod;
import org.herts.core.serializer.HertsSerializer;
import org.herts.core.service.HertsService;
import org.herts.metrics.HertsMetrics;

import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Herts rpc Bidirectional streaming Method Handler
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsRpcBMethodHandler<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

    private final Object coreObject;
    private final Object[] requests;
    private final Method reflectMethod;
    private final HertsMethod hertsMethod;
    private final HertsRpcCaller hertsRpcCaller;

    /**
     * Initialize HertsRpcBMethodHandler
     *
     * @param hertsMethod  HertsMethod
     * @param hertsMetrics HertsMetrics
     * @param hertsService HertsService
     */
    public HertsRpcBMethodHandler(HertsMethod hertsMethod, HertsMetrics hertsMetrics, HertsService hertsService) {
        this.hertsMethod = hertsMethod;
        this.requests = new Object[this.hertsMethod.getParameters().length];
        this.coreObject = hertsService;

        Class<?> coreClass = hertsService.getClass();
        Method method;
        try {
            method = coreClass.getMethod(hertsMethod.getMethodName(), hertsMethod.getParameters());
        } catch (NoSuchMethodException ex) {
            throw new HertsInstanceException(ex);
        }

        this.reflectMethod = method;
        HertsSerializer serializer = new HertsSerializer();
        if (hertsMetrics != null && hertsMetrics.isMetricsEnabled()) {
            this.hertsRpcCaller = new HertsRpcMetricsCaller(this.reflectMethod, hertsMetrics, serializer, coreObject, requests);
        } else {
            this.hertsRpcCaller = new HertsRpcSimpleCaller(this.reflectMethod, serializer, coreObject, requests);
        }
    }

    @Override
    public StreamObserver<Req> invoke(StreamObserver<Resp> responseObserver) {
        try {
            StreamObserver<Resp> response = this.hertsRpcCaller.invokeStreaming(this.coreObject, responseObserver);
            return (StreamObserver<Req>) response;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new HertsMessageException(ex);
        }
    }

    @Override
    public void invoke(Req request, StreamObserver<Resp> responseObserver) {
        throw new AssertionError();
    }
}
