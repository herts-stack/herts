package org.herts.http;

import org.herts.core.context.HertsMetricsSetting;
import org.herts.core.exception.HertsHttpBuildException;
import org.herts.core.logger.HertsLogger;
import org.herts.core.service.HertsService;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.HertsMetricsHandler;
import org.herts.metrics.HertsMetricsServer;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Herts http server implementation
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpServer implements HertsHttpEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsHttpEngine.class.getSimpleName());
    private static final String[] HETRS_HTTP_METHODS = new String[]{"POST", "OPTIONS"};

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

            List<String> endpointLogs = new ArrayList<>();

            for (HertsService coreService : this.hertsRpcServices) {
                HertsMetrics metrics;
                if (this.metricsSetting != null) {
                    metrics = HertsMetricsHandler.builder()
                            .registerHertsServices(Collections.singletonList(coreService))
                            .isErrRateEnabled(this.metricsSetting.isErrRateEnabled())
                            .isJvmEnabled(this.metricsSetting.isJvmEnabled())
                            .isLatencyEnabled(this.metricsSetting.isLatencyEnabled())
                            .isServerResourceEnabled(this.metricsSetting.isServerResourceEnabled())
                            .isRpsEnabled(this.metricsSetting.isRpsEnabled())
                            .build();
                } else {
                    metrics = HertsMetricsHandler.builder()
                            .registerHertsServices(Collections.singletonList(coreService))
                            .build();
                }

                HertsMetricsServer metricsServer = new HertsMetricsServer(metrics, server);
                HertsHttpServerCoreImpl hertsServer = new HertsHttpServerCoreImpl(coreService, metrics, metricsServer);

                endpointLogs.add(coreService.getClass().getSimpleName() + " endpoint.");
                for (String endpoint : hertsServer.getEndpoints()) {
                    for (String m : HETRS_HTTP_METHODS) {
                        String logM = m.equals(HETRS_HTTP_METHODS[0]) ? "[" + m + "]    " : "[" + m + "] ";
                        endpointLogs.add(logM + endpoint);
                    }
                }
                context.addServlet(new ServletHolder(hertsServer), hertsServer.getBaseEndpoint() + "/*");
            }

            if (this.interceptors != null && this.interceptors.size() > 0) {
                context.addFilter(new FilterHolder(
                        new HertsHttpInterceptHandler(this.interceptors)), "/*", EnumSet.of(DispatcherType.REQUEST));
            }

            if (this.sslContextFactory != null) {
                final ServerConnector httpsConnector = new ServerConnector(server, (SslContextFactory.Server) this.sslContextFactory);
                httpsConnector.setPort(this.port);
                server.setConnectors(new Connector[]{httpsConnector});
            }

            for (String log : endpointLogs) {
                logger.info(log);
            }
            server.setHandler(context);
            server.start();
            logger.info("Started Herts HTTP server. Port " + this.port);
            server.join();
        } catch (Exception ex) {
            throw new HertsHttpBuildException(ex);
        }
    }

    @Override
    public void stop() throws Exception {
        if (this.server != null) {
            this.server.stop();
        }
    }
}
