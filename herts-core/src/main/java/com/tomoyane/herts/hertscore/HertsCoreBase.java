package com.tomoyane.herts.hertscore;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsService;

import io.grpc.MethodDescriptor;

public class HertsCoreBase implements HertsService {
    private final HertsCoreType coreType;

    public HertsCoreBase(HertsCoreType rpcType) {
        this.coreType = rpcType;
    }

    @Override
    public HertsCoreType getHertsCoreType() {
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

    protected void setTest() {
    }
}
