package org.herts.core.service;

/**
 * Load balancing broker builder.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ReactiveBrokerTypeBuilder implements HertsReactiveBrokerTypeBuilder {
    private LoadBalancingType loadBalancingType;
    private String connectionInfo;

    public static HertsReactiveBrokerTypeBuilder builder() {
        return new ReactiveBrokerTypeBuilder();
    }

    @Override
    public HertsReactiveBrokerTypeBuilder loadBalancingType(LoadBalancingType loadBalancingType) {
        this.loadBalancingType = loadBalancingType;
        return this;
    }

    @Override
    public HertsReactiveBrokerTypeBuilder connectionInfo(String connectionInfo) {
        this.connectionInfo = connectionInfo;
        return this;
    }

    @Override
    public HertsReactiveBroker build() {
        if (this.loadBalancingType == LoadBalancingType.LocalGroupRepository) {
            return ConcurrentLocalBroker.getInstance();
        } else if (this.loadBalancingType == LoadBalancingType.RedisGroupRepository) {
            // TODO: Imple
            return null;
        }
        return null;
    }
}
