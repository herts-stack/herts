package org.hertsstack.httpclient;

import org.hertsstack.core.service.HertsService;

import java.util.Map;

/**
 * Herts http client
 *
 * @author Herts Contributer
 */
interface HertsHttpClientBase {

    /**
     * Create Hert http server interface
     *
     * @param classType Class type
     * @param <T>       Generics
     * @return HertsCoreService
     */
    <T extends HertsService> T createHertsService(Class<T> classType);

    /**
     * Create Hert http server interface
     *
     * @param classType    Class type
     * @param customHeader Custom header
     * @param <T>          Generics
     * @return HertsCoreService
     */
    <T extends HertsService> T createHertsService(Class<T> classType, Map<String, String> customHeader);

    /**
     * Recreate Hert http server interface
     *
     * @param classType Class type
     * @param <T>       Generics
     * @return HertsCoreService
     */
    <T extends HertsService> T recreateHertsService(Class<T> classType);

    /**
     * Recreate Hert http server interface
     *
     * @param classType    Class type
     * @param customHeader Custom header
     * @param <T>          Generics
     * @return HertsCoreService
     */
    <T extends HertsService> T recreateHertsService(Class<T> classType, Map<String, String> customHeader);

}
