package org.herts.common.service;

import org.herts.common.context.HertsType;

/**
 * Herts server streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HertsServerStreamingService<T> extends HertsServiceBase<T> {
    private T t;

    public HertsServerStreamingService() {
        super(HertsType.ServerStreaming);
    }
}
