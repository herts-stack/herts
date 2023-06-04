package org.herts.common.reactive;

import io.grpc.MethodDescriptor;
import org.herts.common.context.HertsSystemContext;
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
    public String getConnection() {
        return HertsSystemContext.Header.HERTS_CONNECTION_ID_CTX.get();
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
