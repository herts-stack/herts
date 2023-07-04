package org.hertsstack.core.service;

import org.hertsstack.core.context.HertsType;

/**
 * Herts client streaming service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HertsServiceClientStreaming<T> extends ServiceBase<T> {
    private T t;

    public HertsServiceClientStreaming() {
        super(HertsType.ClientStreaming);
    }
}
