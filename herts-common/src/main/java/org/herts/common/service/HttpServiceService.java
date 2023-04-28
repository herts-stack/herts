package org.herts.common.service;

import org.herts.common.context.HertsType;

/**
 * Herts Http server service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class HttpServiceService<T> extends HertsServiceBase<T> {

    public HttpServiceService() {
        super(HertsType.Http);
    }
}
