package com.tomoyane.herts.httpclient;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

/**
 * Herts http client
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpClientBase {

    /**
     * Create Hert http server interface
     * @param classType Class type
     * @return HertsCoreService
     * @param <T> Generics
     */
    <T extends HertsCoreService> T createHertHttpCoreInterface(Class<T> classType);
}
