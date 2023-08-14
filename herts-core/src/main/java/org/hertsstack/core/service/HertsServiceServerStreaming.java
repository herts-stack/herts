package org.hertsstack.core.service;

import org.hertsstack.core.context.HertsType;

/**
 * Herts server streaming service
 *
 * @author Herts Contributer
 */
public abstract class HertsServiceServerStreaming<T> extends ServiceBase<T> {
    private T t;

    public HertsServiceServerStreaming() {
        super(HertsType.ServerStreaming);
    }
}
