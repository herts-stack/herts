package org.herts.common.service;

import io.grpc.MethodDescriptor;
import org.herts.common.context.HertsType;

public class HertsDuplexServiceBase<T, K> implements HertsDuplexService {
    private final HertsType coreType;
    private Class<?> service;
    private Class<?> receiver;

    public HertsDuplexServiceBase(HertsType rpcType) {
        this.coreType = rpcType;
    }

    @Override
    public HertsType getHertsType() {
        return coreType;
    }

    @Override
    public MethodDescriptor.MethodType getGrpcMethodType() {
        return coreType.convertToMethodType();
    }

    @Override
    public String[] getConnections() {
        return new String[0];
    }

    @Override
    public Class<?> getInterface() {
        return null;
    }

    @Override
    public Class<?> getService() {
        return this.service;
    }

    @Override
    public Class<?> getReceiver() {
        return this.receiver;
    }

    protected void setService(Class<?> service) {
        this.service = service;
    }

    protected void setReceiver(Class<?> receiver) {
        this.receiver = receiver;
    }
}
