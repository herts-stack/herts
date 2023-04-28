package org.herts.common.service;

import org.herts.common.context.HertsType;

/**
 * Herts unary streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class UnaryService<T> extends HertsServiceBase<T> {
    private T t;

    public UnaryService() {
        super(HertsType.Unary);
    }
}
