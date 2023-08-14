package org.hertsstack.http;

import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.HttpServerBuildException;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.service.HertsService;
import org.hertsstack.core.util.ServerUtil;
import org.hertsstack.metrics.HertsMetrics;
import org.hertsstack.metrics.HertsMetricsHandler;
import org.hertsstack.metrics.HertsMetricsServer;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Herts http server implementation
 *
 * @author Herts Contributer
 */
public class HertsHttpServer implements HertsHttpEngine {
    private static final java.util.logging.Logger logger = Logging.getLogger(HertsHttpEngine.class.getSimpleName());

    private final List<HertsService> hertsRpcServices;
    private final Map<String, HertsHttpInterceptor> interceptors;
    private final SslContextFactory sslContextFactory;
    private final HertsMetricsSetting metricsSetting;
    private final int port;

    private Server server = null;

    public HertsHttpServer(HertsHttpEngineBuilder builder) {
        this.hertsRpcServices = builder.getHertsRpcServices();
        this.interceptors = builder.getInterceptors();
        this.sslContextFactory = builder.getSslContextFactory();
        this.metricsSetting = builder.getMetricsSetting();
        this.port = builder.getPort();
    }

    public static HertsHttpEngineBuilder builder() {
        return new ServerBuilder();
    }

    @Override
    public void start() {
        try {
            this.server = new Server(this.port);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            // Metrics
            HertsMetrics metrics;
            if (this.metricsSetting != null) {
                metrics = HertsMetricsHandler.builder(HertsType.Http)
                        .registerHertsServices(this.hertsRpcServices)
                        .isErrRateEnabled(this.metricsSetting.isErrRateEnabled())
                        .isJvmEnabled(this.metricsSetting.isJvmEnabled())
                        .isLatencyEnabled(this.metricsSetting.isLatencyEnabled())
                        .isServerResourceEnabled(this.metricsSetting.isServerResourceEnabled())
                        .isRpsEnabled(this.metricsSetting.isRpsEnabled())
                        .build();
            } else {
                metrics = HertsMetricsHandler.builder(HertsType.Http)
                        .registerHertsServices(this.hertsRpcServices)
                        .build();
            }

            HertsMetricsServer hertsMetricsServer = new HertsMetricsServer(metrics, this.server);
            InternalHttpServlet hertsServlet = InternalHttpServlet.createByHertsService(
                    this.hertsRpcServices, metrics, hertsMetricsServer, null);

            context.addServlet(new ServletHolder(hertsServlet), "/*");

            // Interceptor
            context.addFilter(new FilterHolder(
                    new InternalHttpInterceptHandler(this.interceptors, true)), "/*", EnumSet.of(DispatcherType.REQUEST));

            // TLS
            if (this.sslContextFactory != null) {
                final ServerConnector httpsConnector = new ServerConnector(this.server, (SslContextFactory.Server) this.sslContextFactory);
                httpsConnector.setPort(this.port);
                this.server.setConnectors(new Connector[]{httpsConnector});
            }

            for (String log : ServerUtil.getEndpointLogs(hertsServlet.getEndpoints(), this.metricsSetting)) {
                logger.info(log);
            }
            this.server.setHandler(context);
            this.server.start();
            logger.info("Started Herts HTTP server. Port " + this.port);
            this.server.join();

        } catch (Exception ex) {
            throw new HttpServerBuildException(ex);
        }
    }

    @Override
    public void stop() throws Exception {
        if (this.server != null) {
            this.server.stop();
        }
    }
}
