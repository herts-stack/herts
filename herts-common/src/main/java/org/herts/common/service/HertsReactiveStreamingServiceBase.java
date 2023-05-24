package org.herts.common.service;

import io.grpc.MethodDescriptor;
import org.herts.common.context.HertsType;

/**
 * HertsReactiveStreamingServiceBase
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsReactiveStreamingServiceBase<T, K> implements HertsReactiveService {
    private final HertsType coreType;
    private final HertsBroadCaster broadCaster;

    public HertsReactiveStreamingServiceBase(HertsType rpcType) {
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
