package org.herts.common.service;

import io.grpc.MethodDescriptor;
import org.herts.common.context.HertsType;

public class HertsDuplexServiceBase<T, K> implements HertsDuplexService {
    private final HertsType coreType;
    private final HertsBroadCaster broadCaster;

    public HertsDuplexServiceBase(HertsType rpcType) {
        this.coreType = rpcType;
        this.broadCaster = new HertsBroadCasterImpl();
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
        return this.broadCaster.getService();
    }

    @Override
    public Class<?> getReceiver() {
        return this.broadCaster.getReceiver();
    }

    public HertsBroadCaster getBroadCaster() {
        return this.broadCaster;
    }
}
