package com.tomoyane.herts.hertscore.handler;

import com.tomoyane.herts.hertscommon.exception.HertsInstanceException;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.marshaller.HertsMethod;

import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HertsCoreCStreamingMethodHandler<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

    private final Object coreObject;
    private final Object[] requests;
    private final Method reflectMethod;
    private final HertsMethod hertsMethod;

    public HertsCoreCStreamingMethodHandler(HertsMethod hertsMethod) {
        this.hertsMethod = hertsMethod;
        this.requests = new Object[this.hertsMethod.getParameters().length];

        String serviceName = hertsMethod.getCoreServiceName();
        Class<?> coreClass;
        try {
            coreClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ex) {
            throw new HertsRpcNotFoundException("Unknown Herts core class. " + ex.getMessage());
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
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        this.reflectMethod = method;
    }

    @Override
    public StreamObserver<Req> invoke(StreamObserver<Resp> responseObserver) {
        try {
            var response = this.reflectMethod.invoke(this.coreObject, responseObserver);
            return (StreamObserver<Req>) response;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void invoke(Req request, StreamObserver<Resp> responseObserver) {
        throw new AssertionError();
    }
}
