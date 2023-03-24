package com.tomoyane.herts.hertscore.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tomoyane.herts.hertscommon.exception.HertsInstanceException;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.marshaller.HertsMethod;
import com.tomoyane.herts.hertscommon.marshaller.HertsMsg;
import io.grpc.stub.StreamObserver;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HertsCoreServerStreamingMethodHandler<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
    private final Object coreObject;
    private final Object[] requests;
    private final Method reflectMethod;
    private final HertsMethod hertsMethod;

    public HertsCoreServerStreamingMethodHandler(HertsMethod hertsMethod) {
        this.hertsMethod = hertsMethod;
        this.requests = new Object[this.hertsMethod.getParameters().length];
//        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

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
        throw new AssertionError();
    }

    @Override
    public void invoke(Req request, StreamObserver<Resp> responseObserver) {
        try {
            if (((byte[]) request).length > 0) {
                HertsMsg deserialized = this.objectMapper.readValue((byte[]) request, HertsMsg.class);
                var index = 0;
                for (Object obj : deserialized.getMessageParameters()) {
                    var castType = deserialized.getClassTypes()[index];
                    this.requests[index] = this.objectMapper.convertValue(obj, castType);
                    index++;
                }
                this.requests[this.requests.length-1] =  (StreamObserver<Object>) responseObserver;
                System.out.println(this.reflectMethod);
                for (Object o : this.requests) {
                    System.out.println(o.getClass().getSimpleName());
                }
                var a = this.reflectMethod.invoke(this.coreObject, this.requests);
            } else {
                var b = this.reflectMethod.invoke(this.coreObject, responseObserver);
            }

        } catch (IllegalAccessException | IOException ex) {
            ex.printStackTrace();
            responseObserver.onError(ex);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            Throwable cause = ex.getCause();
            responseObserver.onError(cause);
        }
    }
}
