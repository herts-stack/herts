package com.tomoyane.herts.hertscore.handler;

import com.tomoyane.herts.hertscommon.context.HertsMsg;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HertsCoreSimpleCaller implements HertsCoreCaller {
    private final Method reflectMethod;
    private final HertsSerializer hertsSerializer;
    private final Object coreObject;
    private final Object[] requests;

    public HertsCoreSimpleCaller(Method reflectMethod, HertsSerializer hertsSerializer, Object coreObject, Object[] requests) {
        this.reflectMethod = reflectMethod;
        this.hertsSerializer = hertsSerializer;
        this.coreObject = coreObject;
        this.requests = requests;
    }

    @Override
    public <T> StreamObserver<T> invokeStreaming(Object obj, StreamObserver<T> responseObserver) throws InvocationTargetException, IllegalAccessException {
        return (StreamObserver<T>) this.reflectMethod.invoke(obj, responseObserver);
    }

    @Override
    public <T, K> Object invokeServerStreaming(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        if (((byte[]) request).length > 0) {
            HertsMsg deserialized = this.hertsSerializer.deserialize((byte[]) request, HertsMsg.class);
            var index = 0;
            for (Object obj : deserialized.getMessageParameters()) {
                var castType = deserialized.getClassTypes()[index];
                this.requests[index] = this.hertsSerializer.convert(obj, castType);
                index++;
            }
            this.requests[this.requests.length-1] =  (StreamObserver<Object>) responseObserver;
            return this.reflectMethod.invoke(this.coreObject, this.requests);
        } else {
            return this.reflectMethod.invoke(this.coreObject, responseObserver);
        }
    }

    @Override
    public <T, K> Object invokeUnary(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        if (((byte[]) request).length > 0) {
            HertsMsg deserialized = this.hertsSerializer.deserialize((byte[]) request, HertsMsg.class);
            var index = 0;
            for (Object obj : deserialized.getMessageParameters()) {
                var castType = deserialized.getClassTypes()[index];
                this.requests[index] = this.hertsSerializer.convert(obj, castType);
                index++;
            }

            return this.reflectMethod.invoke(this.coreObject, this.requests);
        } else {
            return this.reflectMethod.invoke(this.coreObject);
        }
    }
}
