package org.herts.http.engine;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.service.HertsService;
import org.herts.http.HertsHttpInterceptor;

import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.util.List;

/**
 * Herts http server engine builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsHttpEngineBuilder {

    /**
     * Set interceptor
     * @param interceptor HertsHttpInterceptor
     * @return HertHttpEngineBuilder
     */
    HertsHttpEngineBuilder setInterceptor(HertsHttpInterceptor interceptor);

    /**
     * Add implementation service
     * Not interface
     * @param hertsRpcService HertsCoreService
     * @return HertHttpEngineBuilder
     */
    HertsHttpEngineBuilder addImplementationService(HertsService hertsRpcService);

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
    HertsHttpEngineBuilder setSsl(SslContextFactory sslContextFactory, int port);

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
    HertsHttpInterceptor getInterceptor();

    /**
     * Get Metrics setting
     * @return HertsMetricsSetting
     */
    HertsMetricsSetting getMetricsSetting();

    /**
     * Get ssl context
     * @return SslContextFactory
     */
    SslContextFactory getSslContextFactory();

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
