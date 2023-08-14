package org.hertsstack.core.service;

import org.hertsstack.core.context.HertsType;

/**
 * Herts unary streaming service
 *
 * @author Herts Contributer
 */
public abstract class HertsServiceUnary<T> extends ServiceBase<T> {
    private T t;

    public HertsServiceUnary() {
        super(HertsType.Unary);
    }
}
