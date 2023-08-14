package org.hertsstack.gateway;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.HttpServerBuildException;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.util.ServerUtil;
import org.hertsstack.http.HertsHttpInterceptor;
import org.hertsstack.http.InternalHttpInterceptHandler;
import org.hertsstack.http.InternalHttpServlet;
import org.hertsstack.metrics.HertsMetrics;
import org.hertsstack.metrics.HertsMetricsHandler;
import org.hertsstack.metrics.HertsMetricsServer;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Herts gateway engine implementation.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsGatewayEngineImpl implements HertsGatewayEngine {
    private static final java.util.logging.Logger logger = Logging.getLogger(HertsGatewayEngine.class.getSimpleName());

    private final List<Class<?>> hertsServiceInterfaces;
    private final Map<String, HertsHttpInterceptor> interceptorMap;
    private final HertsMetricsSetting metricsSetting;
    private final SslContextFactory gatewaySslContextFactory;
    private final int gatewayPort;
    private final int rpcPort;
    private final String rpcHost;
    private final boolean isRpcSecure;

    private Server server = null;

    public HertsGatewayEngineImpl(HertsGatewayBuilder builder) {
        this.hertsServiceInterfaces = builder.getHertsServiceInterfaces();
        this.interceptorMap = builder.getInterceptorMap();
        this.metricsSetting = builder.getMetricsSetting();
        this.gatewaySslContextFactory = builder.getGatewaySslContextFactory();
        this.gatewayPort = builder.getGatewayPort();
        this.rpcPort = builder.getRpcPort();
        this.rpcHost = builder.getRpcHost();
        this.isRpcSecure = builder.isRpcSecure();
    }

    @Override
    public void start() {
        try {
            this.server = new Server(this.gatewayPort);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            // Metrics
            HertsMetrics metrics;
            if (this.metricsSetting != null) {
                metrics = HertsMetricsHandler.builder(HertsType.Http)
                        .registerHertsServicesByInterface(this.hertsServiceInterfaces)
                        .isErrRateEnabled(this.metricsSetting.isErrRateEnabled())
                        .isJvmEnabled(this.metricsSetting.isJvmEnabled())
                        .isLatencyEnabled(this.metricsSetting.isLatencyEnabled())
                        .isServerResourceEnabled(this.metricsSetting.isServerResourceEnabled())
                        .isRpsEnabled(this.metricsSetting.isRpsEnabled())
                        .build();
            } else {
                metrics = HertsMetricsHandler.builder(HertsType.Http)
                        .registerHertsServicesByInterface(this.hertsServiceInterfaces)
                        .build();
            }

            RpcClient rpcClient = RpcClient.create(this.rpcHost, this.isRpcSecure, this.rpcPort, this.hertsServiceInterfaces);
            HertsMetricsServer hertsMetricsServer = new HertsMetricsServer(metrics, this.server);
            InternalHttpServlet hertsServlet = InternalHttpServlet.createGateway(
                    this.hertsServiceInterfaces, metrics, new HttpServerCaller(hertsMetricsServer, rpcClient));

            context.addServlet(new ServletHolder(hertsServlet), "/*");

            // Interceptor
            context.addFilter(new FilterHolder(
                    new InternalHttpInterceptHandler(this.interceptorMap, false)), "/*", EnumSet.of(DispatcherType.REQUEST));

            // TLS
            if (this.gatewaySslContextFactory != null) {
                final ServerConnector httpsConnector = new ServerConnector(this.server, (SslContextFactory.Server) this.gatewaySslContextFactory);
                httpsConnector.setPort(this.gatewayPort);
                server.setConnectors(new Connector[]{httpsConnector});
            }

            for (String log : ServerUtil.getEndpointLogs(hertsServlet.getEndpoints(), this.metricsSetting)) {
                logger.info(log);
            }
            server.setHandler(context);
            logger.info("Started Herts Gateway server. Port " + this.gatewayPort);
            server.start();
            server.join();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new HttpServerBuildException(ex);
        }
    }

    @Override
    public void stop() {
        if (this.server != null) {
            try {
                this.server.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
