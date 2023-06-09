package org.herts.rpc;

import org.herts.core.exception.ServiceMethodNotfoundException;
import org.herts.core.modelx.RegisteredMethod;
import org.herts.serializer.MessageSerializer;

import io.grpc.stub.StreamObserver;
import org.herts.core.service.HertsService;
import org.herts.metrics.HertsMetrics;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Herts rpc Server streaming Method Handler
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsRpcSStreamingMethodHandler<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

    private final MessageSerializer serializer = new MessageSerializer();
    private final Object coreObject;
    private final Object[] requests;
    private final Method reflectMethod;
    private final RegisteredMethod hertsMethod;
    private final HertsRpcCaller hertsRpcCaller;

    public HertsRpcSStreamingMethodHandler(RegisteredMethod hertsMethod, HertsMetrics hertsMetrics, HertsService hertsService) {
        this.hertsMethod = hertsMethod;
        this.requests = new Object[this.hertsMethod.getParameters().length];
        this.coreObject = hertsService;

        Class<?> coreClass = hertsService.getClass();
        Method method;
        try {
            method = coreClass.getMethod(hertsMethod.getMethodName(), hertsMethod.getParameters());
        } catch (NoSuchMethodException ex) {
            throw new ServiceMethodNotfoundException(ex);
        }

        this.reflectMethod = method;
        if (hertsMetrics != null && hertsMetrics.isMetricsEnabled()) {
            this.hertsRpcCaller = new HertsRpcMetricsCaller(this.reflectMethod, hertsMetrics, serializer, coreObject, requests);
        } else {
            this.hertsRpcCaller = new HertsRpcSimpleCaller(this.reflectMethod, serializer, coreObject, requests);
        }
    }

    @Override
    public StreamObserver<Req> invoke(StreamObserver<Resp> responseObserver) {
        throw new AssertionError();
    }

    @Override
    public void invoke(Req request, StreamObserver<Resp> responseObserver) {
        try {
            this.hertsRpcCaller.invokeServerStreaming(request, responseObserver);
        } catch (IllegalAccessException | IOException ex) {
            responseObserver.onError(ex);
        } catch (InvocationTargetException ex) {
            responseObserver.onError(ex.getCause());
        }
    }
}
