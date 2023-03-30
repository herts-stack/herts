package com.tomoyane.herts.hertscore;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsClientService;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import io.grpc.MethodDescriptor;
import io.grpc.stub.StreamObserver;

public class HertsCoreCoreBase implements HertsCoreService {
    private final HertsCoreType coreType;

    public HertsCoreCoreBase(HertsCoreType rpcType) {
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

//    public <T> HertsClientService Broadcast(String connectionId) {
//        return
//    }
}
