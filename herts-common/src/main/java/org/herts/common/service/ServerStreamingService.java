package org.herts.common.service;

import org.herts.common.context.HertsType;

/**
 * Herts server streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class ServerStreamingService<T> extends HertsServiceBase<T> {
    private T t;

    public ServerStreamingService() {
        super(HertsType.ServerStreaming);
    }
}
