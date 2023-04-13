package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsType;

import io.grpc.MethodDescriptor;

public class HertsCoreCoreBase implements HertsCoreService {
    private final HertsType coreType;

    public HertsCoreCoreBase(HertsType rpcType) {
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
