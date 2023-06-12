package org.herts.core.service;

import org.herts.core.context.HertsType;

/**
 * Herts Http server service
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HertsServiceHttp<T> extends ServiceBase<T> {

    public HertsServiceHttp() {
        super(HertsType.Http);
    }
}
