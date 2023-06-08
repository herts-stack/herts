package org.herts.core.service;

import io.grpc.MethodDescriptor;
import org.herts.core.context.HertsSystemContext;
import org.herts.core.context.HertsType;

/**
 * HertsReactiveStreamingServiceBase
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsReactiveStreamingServiceBase<T, K> implements HertsReactiveService {
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

    public void createBroker(LoadBalancingType lbType, String connectionInfo) {
        HertsReactiveBroker broker = ReactiveBrokerTypeBuilder.builder()
                .loadBalancingType(lbType)
                .connectionInfo(connectionInfo)
                .build();
        this.broadCaster.setBroker(broker);
    }

    public HertsBroadCaster getBroadCaster() {
        return this.broadCaster;
    }
}