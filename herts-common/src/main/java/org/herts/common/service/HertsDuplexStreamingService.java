package org.herts.common.service;

import io.grpc.stub.StreamObserver;
import org.herts.common.cache.DuplexStreamingCache;
import org.herts.common.cache.DuplexStreamingCacheImpl;
import org.herts.common.context.HertsClientInfo;
import org.herts.common.context.HertsType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Herts duplex streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsDuplexStreamingService<T, K> extends HertsDuplexServiceBase<T, K> implements HertsDuplexInternalStreaming {
    private final DuplexStreamingCache duplexStreamingCache = DuplexStreamingCacheImpl.getInstance();
    private final Class<?> serviceT;
    private final Class<?> receiverK;

    private HertsDuplexService proxyReceiver = null;
    private String clientId;

    public HertsDuplexStreamingService() {
        super(HertsType.DuplexStreaming);

        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        this.serviceT = (Class<?>) typeArguments[0];
        this.receiverK = (Class<?>) typeArguments[1];
        setService(this.serviceT);
        setReceiver(this.receiverK);
    }

    public String getClientId() {
        return clientId;
    }

    public Class<?> getService() {
        return this.serviceT;
    }

    private void createReceiver() {
        StreamObserver<Object[]> observer = this.duplexStreamingCache.getObserver(this.clientId);
        InvocationHandler handler = new HertsDuplexStreamingInvoker(observer);
        this.proxyReceiver = (HertsDuplexService) Proxy.newProxyInstance(
                this.receiverK.getClassLoader(),
                new Class<?>[]{ this.receiverK },
                handler);
    }

    protected K broadcast(String clientId) {
        if (this.duplexStreamingCache == null) {
            return null;
        }
        if (this.proxyReceiver == null) {
            createReceiver();
        }
        return (K) this.proxyReceiver;
    }

    @Override
    public void registerReceiver(HertsClientInfo clientInfo, StreamObserver<Object[]> objectStreamObservers) {
        this.duplexStreamingCache.setClientInfo(clientInfo);
        this.duplexStreamingCache.registerObserverToServer(clientInfo.id, objectStreamObservers);
        this.clientId = clientInfo.id;
        createReceiver();
    }
}
