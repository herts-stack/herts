package org.herts.core.service;

import org.herts.core.context.HertsType;

/**
 * Herts unary streaming service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HertsServiceUnary<T> extends ServiceBase<T> {
    private T t;

    public HertsServiceUnary() {
        super(HertsType.Unary);
    }
}
