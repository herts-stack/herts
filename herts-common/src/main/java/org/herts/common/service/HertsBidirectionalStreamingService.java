package org.herts.common.service;

import org.herts.common.context.HertsType;

/**
 * Herts bidirectional streaming service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HertsBidirectionalStreamingService<T> extends HertsServiceBase<T> {
    private T t;

    public HertsBidirectionalStreamingService() {
        super(HertsType.BidirectionalStreaming);
    }
}
