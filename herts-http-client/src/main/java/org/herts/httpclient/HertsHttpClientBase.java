package org.herts.httpclient;

import org.herts.common.service.HertsService;

import java.util.Map;

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
    <T extends HertsService> T createHertsService(Class<T> classType);

    /**
     * Create Hert http server interface
     * @param classType Class type
     * @param customHeader Custom header
     * @return HertsCoreService
     * @param <T> Generics
     */
    <T extends HertsService> T createHertsService(Class<T> classType, Map<String, String> customHeader);

    /**
     * Recreate Hert http server interface
     * @param classType Class type
     * @return HertsCoreService
     * @param <T> Generics
     */
    <T extends HertsService> T recreateHertsService(Class<T> classType);

    /**
     * Recreate Hert http server interface
     * @param classType Class type
     * @param customHeader Custom header
     * @return HertsCoreService
     * @param <T> Generics
     */
    <T extends HertsService> T recreateHertsService(Class<T> classType, Map<String, String> customHeader);

}
