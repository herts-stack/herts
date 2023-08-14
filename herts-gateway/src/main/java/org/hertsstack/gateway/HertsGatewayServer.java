package org.hertsstack.gateway;

import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.http.HertsHttpInterceptor;

import javax.annotation.Nullable;

/**
 * Herts gateway server builder interface.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsGatewayServer {

    /**
     * Register gateway rpc server interface.
     * Allowed @HertsRpcService with Unary only.
     *
     * @param hertsServiceInterface HertsService interface
     * @param interceptor HertsHttpInterceptor
     * @return HertsGatewayServer
     */
    HertsGatewayServer registerHertsRpcService(Class<?> hertsServiceInterface, @Nullable HertsHttpInterceptor interceptor);

    /**
     * Gateway server port.
     *
     * @param port Port number
     * @return HertsGatewayServer
     */
    HertsGatewayServer gatewayPort(int port);

    /**
     * Target RPC server host.
     *
     * @param host Host
     * @return HertsGatewayServer
     */
    HertsGatewayServer rpcHost(String host);

    /**
     * Target RPC port number.
     *
     * @param port Port number
     * @return HertsGatewayServer
     */
    HertsGatewayServer rpcPort(int port);

    /**
     * Enable metrics.
     *
     * @param metricsSetting HertsMetricsSetting
     * @return HertsGatewayServer
     */
    HertsGatewayServer enableMetrics(HertsMetricsSetting metricsSetting);

    /**
     * RPC server secure or not.
     *
     * @param isRpcSecure IsSSL
     * @return HertsGatewayServer
     */
    HertsGatewayServer rpcSecure(boolean isRpcSecure);

    /**
     * Gateway server ssl setting.
     *
     * @param sslContextFactory org.eclipse.jetty.util.ssl.SslContextFactory
     * @return HertsGatewayServer
     */
    HertsGatewayServer gatewaySsl(org.eclipse.jetty.util.ssl.SslContextFactory sslContextFactory);

    /**
     * Build HertsGatewayEngine.
     *
     * @return HertsGatewayEngine
     */
    HertsGatewayEngine build();
}