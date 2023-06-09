package org.herts.rpc;

import org.herts.core.serializer.MessageSerializer;

import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Herts rpc with metrics caller.
 * HertsRpcCaller implementation
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsRpcSimpleCaller extends BaseCaller implements HertsRpcCaller {
    private final Method reflectMethod;
    private final Object coreObject;
    private final Object[] requests;

    public HertsRpcSimpleCaller(Method reflectMethod, MessageSerializer hertsSerializer, Object coreObject, Object[] requests) {
        super(reflectMethod, hertsSerializer, coreObject, requests);
        this.reflectMethod = reflectMethod;
        this.coreObject = coreObject;
        this.requests = requests;
    }

    @Override
    public <T> StreamObserver<T> invokeStreaming(Object obj, StreamObserver<T> responseObserver) throws InvocationTargetException, IllegalAccessException {
        return (StreamObserver<T>) this.reflectMethod.invoke(obj, responseObserver);
    }

    @Override
    public <T, K> Object invokeServerStreaming(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        setMethodRequests(request);
        if (((byte[]) request).length > 0) {
            this.requests[this.requests.length - 1] = (StreamObserver<Object>) responseObserver;
            return this.reflectMethod.invoke(this.coreObject, this.requests);
        } else {
            return this.reflectMethod.invoke(this.coreObject, responseObserver);
        }
    }

    @Override
    public <T, K> Object invokeUnary(T request, StreamObserver<K> responseObserver) throws InvocationTargetException, IllegalAccessException, IOException {
        Object res;
        setMethodRequests(request);
        if (this.requests != null && this.requests.length > 0) {
            res = call(this.requests);
        } else {
            res = call(null);
        }
        return res;
    }
}
