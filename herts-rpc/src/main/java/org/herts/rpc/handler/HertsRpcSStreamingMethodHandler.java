package org.herts.rpc.handler;

import org.herts.common.exception.HertsInstanceException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.context.HertsMethod;
import org.herts.common.serializer.HertsSerializer;

import io.grpc.stub.StreamObserver;
import org.herts.common.service.HertsService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HertsRpcSStreamingMethodHandler<Req, Resp> implements
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

    public HertsRpcSStreamingMethodHandler(HertsMethod hertsMethod, HertsService hertsService) {
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
        this.hertsRpcCaller = new HertsRpcSimpleCaller(this.reflectMethod, this.serializer, coreObject, requests);
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
