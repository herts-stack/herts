package org.herts.common.loadbalancing;

/**
 * Herts broker type builder.
 * You can define broker type by this builder.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsBrokerTypeBuilder {

    /**
     * Load balancing type
     * See: LoadBalancingType enum
     *
     * @param loadBalancingType LoadBalancingType
     * @return HertsBrokerTypeBuilder
     */
    HertsBrokerTypeBuilder loadBalancingType(LoadBalancingType loadBalancingType);

    /**
     * Connection info if use solution
     *
     * @param connectionInfo ConeectionInfo
     * @return HertsBrokerTypeBuilder
     */
    HertsBrokerTypeBuilder connectionInfo(String connectionInfo);

    /**
     * Build HertsBroker
     *
     * @return HertsBroker
     */
    HertsBroker build();
}
