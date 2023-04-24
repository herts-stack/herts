package org.herts.httpclient;

import org.herts.common.service.HertsRpcService;

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
    <T extends HertsRpcService> T createHertHttpCoreInterface(Class<T> classType);
}
