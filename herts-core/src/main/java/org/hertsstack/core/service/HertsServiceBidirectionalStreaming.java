package org.hertsstack.core.service;

import org.hertsstack.core.context.HertsType;

/**
 * Herts bidirectional streaming service
 *
 * @author Herts Contributer
 */
public abstract class HertsServiceBidirectionalStreaming<T> extends ServiceBase<T> {
    private T t;

    public HertsServiceBidirectionalStreaming() {
        super(HertsType.BidirectionalStreaming);
    }
}
