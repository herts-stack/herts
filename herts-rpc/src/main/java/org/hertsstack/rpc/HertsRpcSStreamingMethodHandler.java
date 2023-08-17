package org.hertsstack.rpc;

import io.grpc.Context;
import org.hertsstack.core.exception.ServiceMethodNotfoundException;
import org.hertsstack.core.modelx.RegisteredMethod;
import org.hertsstack.serializer.MessageSerializer;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.service.HertsService;
import org.hertsstack.metrics.HertsMetrics;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Herts rpc Server streaming Method Handler
 *
 * @author Herts Contributer
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
        this.requests = new Object[this.hertsMethod.getParameterClasses().length];
        this.coreObject = hertsService;

        Class<?> coreClass = hertsService.getClass();
        Method method;
        try {
            method = coreClass.getMethod(hertsMethod.getMethodName(), hertsMethod.getParameterClasses());
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
        Context newContext = Context.current().fork();
        Context origContext = newContext.attach();
        try {
            this.hertsRpcCaller.invokeServerStreaming(request, responseObserver);
        } catch (IllegalAccessException | IOException ex) {
            responseObserver.onError(ex);
        } catch (InvocationTargetException ex) {
            responseObserver.onError(ex.getCause());
        } finally {
            newContext.detach(origContext);
        }
    }
}
