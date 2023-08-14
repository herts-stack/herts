package org.hertsstack.core.service;

import org.hertsstack.core.context.HertsType;

/**
 * Herts Http server service
 *
 * @author Herts Contributer
 */
public abstract class HertsServiceHttp<T> extends ServiceBase<T> {

    public HertsServiceHttp() {
        super(HertsType.Http);
    }
}
