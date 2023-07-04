package org.hertsstack.core.service;

import io.grpc.MethodDescriptor;
import org.hertsstack.broker.ReactiveBroker;
import org.hertsstack.core.context.SharedServiceContext;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.logger.Logging;

import java.util.logging.Logger;

/**
 * HertsReactiveStreamingServiceBase
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class ReactiveStreamingBase<T, K> implements HertsReactiveService {
    private static final Logger logger = Logging.getLogger(ReactiveStreamingBase.class.getSimpleName());

    private final HertsType coreType;
    private final HertsBroadCaster broadCaster;

    public ReactiveStreamingBase(HertsType rpcType) {
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
        return SharedServiceContext.Header.HERTS_CONNECTION_ID_CTX.get();
    }

    @Override
    public Class<?> getService() {
        return this.broadCaster.getService();
    }

    @Override
    public Class<?> getReceiver() {
        return this.broadCaster.getReceiver();
    }

    public void setBroker(ReactiveBroker broker) {
        logger.info("Setup broker type is " + broker.getBrokerType());
        this.broadCaster.setBroker(broker);
    }

    public HertsBroadCaster getBroadCaster() {
        return this.broadCaster;
    }
}
