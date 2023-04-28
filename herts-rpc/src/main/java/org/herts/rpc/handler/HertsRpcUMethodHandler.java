package org.herts.rpc.handler;

import org.herts.common.exception.HertsInstanceException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.context.HertsMethod;
import org.herts.common.serializer.HertsSerializer;

import org.herts.metrics.HertsMetrics;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HertsRpcUMethodHandler<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

    private final HertsSerializer serializer = new HertsSerializer();
    private final Object coreObject;
    private final Object[] requests;
    private final Method reflectMethod;
    private final HertsMethod hertsMethod;
    private final HertsRpcCaller hertsRpcCaller;

    public HertsRpcUMethodHandler(HertsMethod hertsMethod, HertsMetrics hertsMetrics) {
        this.hertsMethod = hertsMethod;
        this.requests = new Object[this.hertsMethod.getParameters().length];

        String serviceName = hertsMethod.getCoreImplServiceName();
        Class<?> coreClass;
        try {
            coreClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ex) {
            throw new HertsServiceNotFoundException("Unknown Herts core class. " + ex.getMessage());
        }

        try {
            this.coreObject = coreClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HertsInstanceException(e);
        }

        Method method;
        try {
            method = coreClass.getDeclaredMethod(hertsMethod.getMethodName(), hertsMethod.getParameters());
        } catch (NoSuchMethodException ex) {
            throw new HertsInstanceException(ex);
        }

        this.reflectMethod = method;
        if (hertsMetrics.isMetricsEnabled()) {
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
            Object response = this.hertsRpcCaller.invokeUnary(request, responseObserver);
            if (response == null) {
                responseObserver.onNext(null);
                responseObserver.onCompleted();
            } else {
                var responseBytes = this.serializer.serialize(response);
                responseObserver.onNext((Resp) responseBytes);
                responseObserver.onCompleted();
            }
        } catch (IllegalAccessException | IOException ex) {
            responseObserver.onError(ex);
        } catch (InvocationTargetException ex) {
            responseObserver.onError(ex.getCause());
        }
    }
}
