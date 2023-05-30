package org.herts.common.loadbalancing;

import org.herts.common.loadbalancing.local.ConcurrentLocalBroker;

/**
 * Load balancing broker builder.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class BrokerTypeBuilder implements HertsBrokerTypeBuilder {
    private LoadBalancingType loadBalancingType;
    private String connectionInfo;

    public static HertsBrokerTypeBuilder builder() {
        return new BrokerTypeBuilder();
    }

    @Override
    public HertsBrokerTypeBuilder loadBalancingType(LoadBalancingType loadBalancingType) {
        this.loadBalancingType = loadBalancingType;
        return this;
    }

    @Override
    public HertsBrokerTypeBuilder connectionInfo(String connectionInfo) {
        this.connectionInfo = connectionInfo;
        return this;
    }

    @Override
    public HertsBroker build() {
        if (this.loadBalancingType == LoadBalancingType.LocalGroupRepository) {
            return ConcurrentLocalBroker.getInstance();
        } else if (this.loadBalancingType == LoadBalancingType.RedisGroupRepository) {
            // TODO: Imple
            return null;
        }
        return null;
    }
}
