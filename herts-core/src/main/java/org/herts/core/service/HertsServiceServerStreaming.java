package org.herts.core.service;

import org.herts.core.context.HertsType;

/**
 * Herts server streaming service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HertsServiceServerStreaming<T> extends HertsServiceBase<T> {
    private T t;

    public HertsServiceServerStreaming() {
        super(HertsType.ServerStreaming);
    }
}
