package org.herts.common.loadbalancing;

public interface HertsMessageBrokerBuilder {
    HertsMessageBrokerBuilder loadBalancingType(LoadBalancingType loadBalancingType);

    HertsMessageBrokerBuilder connectionInfo(String connectionInfo);

    HertsMessageBroker build();
}
