package org.herts.common.service;

import org.herts.common.context.HertsType;

/**
 * Herts client streaming service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HertsClientStreamingService<T> extends HertsServiceBase<T> {
    private T t;

    public HertsClientStreamingService() {
        super(HertsType.ClientStreaming);
    }
}
