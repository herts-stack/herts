package org.herts.rpc;

import org.herts.core.exception.ServiceMethodNotfoundException;
import org.herts.core.modelx.RegisteredMethod;
import org.herts.core.exception.rpc.RpcErrorException;
import org.herts.core.serializer.MessageSerializer;

import org.herts.core.service.HertsService;
import org.herts.metrics.HertsMetrics;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Herts rpc Unary streaming Method Handler
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsRpcUMethodHandler<Req, Resp> implements
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

    public HertsRpcUMethodHandler(RegisteredMethod hertsMethod, HertsMetrics hertsMetrics, HertsService hertsService) {
        this.hertsMethod = hertsMethod;
        this.requests = new Object[this.hertsMethod.getParameters().length];

        Class<?> coreClass = hertsService.getClass();
        this.coreObject = hertsService;
        Method method;
        try {
            method = coreClass.getDeclaredMethod(hertsMethod.getMethodName(), hertsMethod.getParameters());
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
            Object response = this.hertsRpcCaller.invokeUnary(request, responseObserver);
            if (response == null) {
                responseObserver.onNext((Resp) new byte[]{});
                responseObserver.onCompleted();
            } else {
                byte[] responseBytes = this.serializer.serialize(response);
                responseObserver.onNext((Resp) responseBytes);
                responseObserver.onCompleted();
            }
        } catch (IllegalAccessException | IOException ex) {
            responseObserver.onError(new RpcErrorException(RpcErrorException.StatusCode.Status13, ex.getMessage()).createStatusException());
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RpcErrorException) {
                RpcErrorException exception = (RpcErrorException) cause;
                responseObserver.onError(exception.createStatusException());
            } else {
                responseObserver.onError(new RpcErrorException(RpcErrorException.StatusCode.Status13, "Unexpected error occurred. " + ex.getMessage()).createStatusException());
            }
        }
    }
}
