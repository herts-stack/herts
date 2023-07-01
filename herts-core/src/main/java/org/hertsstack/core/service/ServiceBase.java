package org.hertsstack.core.service;

import org.hertsstack.core.context.SharedServiceContext;
import org.hertsstack.core.context.HertsType;

import io.grpc.MethodDescriptor;

class ServiceBase<T> implements HertsService {
    private T t;
    private final HertsType coreType;

    public ServiceBase(HertsType rpcType) {
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
    public String getConnection() {
        if (this.coreType == HertsType.Http) {
            return null;
        }
        return SharedServiceContext.Header.HERTS_CONNECTION_ID_CTX.get();
    }
}
