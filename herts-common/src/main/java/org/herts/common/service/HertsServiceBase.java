package org.herts.common.service;

import org.herts.common.context.HertsSystemContext;
import org.herts.common.context.HertsType;

import io.grpc.MethodDescriptor;

public class HertsServiceBase<T> implements HertsService {
    private T t;
    private final HertsType coreType;

    public HertsServiceBase(HertsType rpcType) {
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
        return HertsSystemContext.Header.HERTS_CONNECTION_ID_CTX.get();
    }
}
