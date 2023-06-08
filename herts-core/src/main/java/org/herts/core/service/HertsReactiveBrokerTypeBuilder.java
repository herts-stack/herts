package org.herts.core.service;

/**
 * Herts broker type builder.
 * You can define broker type by this builder.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
interface HertsReactiveBrokerTypeBuilder {

    /**
     * Load balancing type
     * See: LoadBalancingType enum
     *
     * @param loadBalancingType LoadBalancingType
     * @return HertsBrokerTypeBuilder
     */
    HertsReactiveBrokerTypeBuilder loadBalancingType(LoadBalancingType loadBalancingType);

    /**
     * Connection info if use solution
     *
     * @param connectionInfo ConeectionInfo
     * @return HertsBrokerTypeBuilder
     */
    HertsReactiveBrokerTypeBuilder connectionInfo(String connectionInfo);

    /**
     * Build HertsBroker
     *
     * @return HertsBroker
     */
    HertsReactiveBroker build();
}
