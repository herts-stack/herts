package org.herts.common.loadbalancing;

import org.herts.common.loadbalancing.local.ConcurrentLocalBroker;

public class BrokerBuilder implements HertsMessageBrokerBuilder {
    private LoadBalancingType loadBalancingType;
    private String connectionInfo;

    public static HertsMessageBrokerBuilder builder() {
        return new BrokerBuilder();
    }

    @Override
    public HertsMessageBrokerBuilder loadBalancingType(LoadBalancingType loadBalancingType) {
        this.loadBalancingType = loadBalancingType;
        return this;
    }

    @Override
    public HertsMessageBrokerBuilder connectionInfo(String connectionInfo) {
        this.connectionInfo = connectionInfo;
        return this;
    }

    @Override
    public HertsMessageBroker build() {
        if (this.loadBalancingType == LoadBalancingType.LocalGroupRepository) {
            return ConcurrentLocalBroker.getInstance();
        } else if (this.loadBalancingType == LoadBalancingType.RedisGroupRepository) {
            // TODO: Imple
            return null;
        }
        return null;
    }
}
