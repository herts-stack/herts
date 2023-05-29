package org.herts.common.loadbalancing;

/**
 * LoadBalancingType
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public enum LoadBalancingType {
    /**
     * Local queue group.
     * This is default type.
     */
    LocalGroupRepository,

    /**
     * Redis queue group.
     * Using pub/sub for internal processing
     */
    RedisGroupRepository
}
