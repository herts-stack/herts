package org.herts.http.engine;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.service.HertsService;
import org.herts.http.HertsHttpInterceptor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Herts http server engine builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpEngineBuilder {

    /**
     * Add implementation service
     * Not interface
     * @param hertsRpcService HertsCoreService
     * @return HertHttpEngineBuilder
     */
    HertsHttpEngineBuilder registerHertsHttpService(HertsService hertsRpcService, @Nullable HertsHttpInterceptor interceptor);

    /**
     * Add implementation service
     * Not interface
     * @param hertsRpcService HertsCoreService
     * @return HertHttpEngineBuilder
     */
    HertsHttpEngineBuilder registerHertsHttpService(HertsService hertsRpcService);


    /**
     * Set port
     * @param port Port
     * @return HertHttpEngineBuilder
     */
    HertsHttpEngineBuilder setPort(int port);

    /**
     * Set ssl for Jetty.
     * @param sslContextFactory SslContextFactory
     * @param port Port
     * @return HertHttpEngineBuilder
     */
    HertsHttpEngineBuilder setSsl(org.eclipse.jetty.util.ssl.SslContextFactory sslContextFactory, int port);

    /**
     * Set metrics setting information
     * @param metricsSetting HertsHttpMetricsSetting
     * @return HertHttpEngineBuilder
     */
    HertsHttpEngineBuilder setMetricsSetting(HertsMetricsSetting metricsSetting);

    /**
     * Get registered HertsRpcService
     * @return HertsRpcService of List
     */
    List<HertsService> getHertsRpcServices();

    /**
     * Get interceptor
     * @return HertsHttpInterceptor
     */
    Map<String, HertsHttpInterceptor> getInterceptors();

    /**
     * Get Metrics setting
     * @return HertsMetricsSetting
     */
    HertsMetricsSetting getMetricsSetting();

    /**
     * Get ssl context
     * @return SslContextFactory
     */
    org.eclipse.jetty.util.ssl.SslContextFactory getSslContextFactory();

    /**
     * Get port number
     * @return integer port
     */
    int getPort();

    /**
     * Build
     * @return HertsHttpEngine
     */
    HertsHttpEngine build();
}
