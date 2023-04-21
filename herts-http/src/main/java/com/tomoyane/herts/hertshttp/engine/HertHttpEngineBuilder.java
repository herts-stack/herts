package com.tomoyane.herts.hertshttp.engine;

import com.tomoyane.herts.hertscommon.context.HertsMetricsSetting;
import com.tomoyane.herts.hertscommon.service.HertsRpcService;
import com.tomoyane.herts.hertshttp.HertsHttpInterceptor;

import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Herts http server engine builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertHttpEngineBuilder {

    /**
     * Set interceptor
     * @param interceptor HertsHttpInterceptor
     * @return HertHttpEngineBuilder
     */
    HertHttpEngineBuilder setInterceptor(HertsHttpInterceptor interceptor);

    /**
     * Add implementation service
     * Not interface
     * @param hertsRpcService HertsCoreService
     * @return HertHttpEngineBuilder
     */
    HertHttpEngineBuilder addImplementationService(HertsRpcService hertsRpcService);

    /**
     * Set port
     * @param port Port
     * @return HertHttpEngineBuilder
     */
    HertHttpEngineBuilder setPort(int port);

    /**
     * Set ssl for Jetty.
     * @param sslContextFactory SslContextFactory
     * @param port Port
     * @return HertHttpEngineBuilder
     */
    HertHttpEngineBuilder setSsl(SslContextFactory sslContextFactory, int port);

    /**
     * Set metrics setting information
     * @param metricsSetting HertsHttpMetricsSetting
     * @return HertHttpEngineBuilder
     */
    HertHttpEngineBuilder setMetricsSetting(HertsMetricsSetting metricsSetting);

    /**
     * Build
     * @return HertsHttpEngine
     */
    HertsHttpEngine build();
}
