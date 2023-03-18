package com.tomoyane.herts.hertscore.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomoyane.herts.hertscommon.exception.HertsInstanceException;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.mapping.HertsMethod;
import com.tomoyane.herts.hertscommon.mapping.HertsMsg;
import io.grpc.stub.StreamObserver;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class HertsCoreMethodHandler<Req, Resp> implements
        io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
        io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
    private final Object coreObject;
    private final Object[] requests;
    private final Method reflectMethod;
    private final HertsMethod hertsMethod;

    public HertsCoreMethodHandler(HertsMethod hertsMethod) {
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
        throw new AssertionError();
    }

    @Override
    public void invoke(Req request, StreamObserver<Resp> responseObserver) {
        try {
            System.out.println("[Debug] Print parameters");
            for (var param : this.hertsMethod.getParameters()) {
                System.out.println(param);
            }

            System.out.println(((byte[])request).length);

            Object response;
            if (((byte[]) request).length > 0) {
                HertsMsg deserialized = this.objectMapper.readValue((byte[]) request, HertsMsg.class);
                var index = 0;
                for (Map.Entry<String, Object> entry : deserialized.getHertsBody().entrySet()) {
                    this.requests[index] = entry.getValue();
                    index++;
                }
                response = this.reflectMethod.invoke(this.coreObject, this.requests);
            } else {
                response = this.reflectMethod.invoke(this.coreObject);
            }

            if (response == null) {
                System.out.println("Invoke response is null");
                responseObserver.onNext(null);
                responseObserver.onCompleted();
            } else {
                System.out.println("Invoke response is not null " + response);
                var responseBytes = this.objectMapper.writeValueAsBytes(response);
                responseObserver.onNext((Resp) responseBytes);
                responseObserver.onCompleted();
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
