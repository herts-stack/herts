package org.herts.common.service;

import org.herts.common.context.HertsType;

import io.grpc.MethodDescriptor;

public class HertsRpcRpcBase implements HertsRpcService {
    private final HertsType coreType;

    public HertsRpcRpcBase(HertsType rpcType) {
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
}
