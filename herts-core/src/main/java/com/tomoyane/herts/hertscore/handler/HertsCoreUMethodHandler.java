package com.tomoyane.herts.hertscore.handler;

import com.tomoyane.herts.hertscommon.exception.HertsInstanceException;
import com.tomoyane.herts.hertscommon.exception.HertsServiceNotFoundException;
import com.tomoyane.herts.hertscommon.context.HertsMethod;
import com.tomoyane.herts.hertscommon.context.HertsMsg;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;

import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HertsCoreUMethodHandler<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

    private final HertsSerializer serializer = new HertsSerializer();
    private final Object coreObject;
    private final Object[] requests;
    private final Method reflectMethod;
    private final HertsMethod hertsMethod;

    public HertsCoreUMethodHandler(HertsMethod hertsMethod) {
        this.hertsMethod = hertsMethod;
        this.requests = new Object[this.hertsMethod.getParameters().length];

        String serviceName = hertsMethod.getCoreServiceName();
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
    }

    @Override
    public StreamObserver<Req> invoke(StreamObserver<Resp> responseObserver) {
        throw new AssertionError();
    }

    @Override
    public void invoke(Req request, StreamObserver<Resp> responseObserver) {
        try {
            Object response;
            if (((byte[]) request).length > 0) {
                HertsMsg deserialized = this.serializer.deserialize((byte[]) request, HertsMsg.class);
                var index = 0;
                for (Object obj : deserialized.getMessageParameters()) {
                    var castType = deserialized.getClassTypes()[index];
                    this.requests[index] = this.serializer.convert(obj, castType);
                    index++;
                }

                response = this.reflectMethod.invoke(this.coreObject, this.requests);
            } else {
                response = this.reflectMethod.invoke(this.coreObject);
            }

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
