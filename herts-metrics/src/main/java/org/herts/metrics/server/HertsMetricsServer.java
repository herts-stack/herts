package org.herts.metrics.server;

import org.herts.common.logger.HertsLogger;
import org.herts.metrics.HertsMetrics;

import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Herts metrics server
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsMetricsServer {
    private static final Logger logger = HertsLogger.getLogger(HertsMetricsServer.class.getSimpleName());
    private final HertsMetrics hertsMetrics;

    private int port = 8888;
    private boolean isCustom = false;
    private boolean isStarted = false;
    private Server server;

    public HertsMetricsServer(HertsMetrics hertsMetrics) {
        this.hertsMetrics = hertsMetrics;
    }

    public HertsMetricsServer(int port, HertsMetrics hertsMetrics) {
        this.port = port;
        this.hertsMetrics = hertsMetrics;
    }

    public HertsMetricsServer(HertsMetrics hertsMetrics, Server server) {
        this.server = server;
        this.isCustom = true;
        this.hertsMetrics = hertsMetrics;
    }

    public void start() throws Exception {
        if (this.isCustom) {
            return;
        }
        if (this.isStarted) {
            return;
        }
        this.isStarted = true;
        this.server = HertsMetricsServerBuilder.builder()
                .hertsServlet(this.hertsMetrics)
                .server(this.port)
                .build();
        this.server.start();
        logger.info("Started Herts metrics server. Port " + this.port);
        this.server.join();
    }

    public void setMetricsResponse(HttpServletResponse response) throws IOException {
        setMetricsResponse(this.hertsMetrics, response);
    }

    public static void setMetricsResponse(HertsMetrics metrics, HttpServletResponse response) throws IOException {
        response.setContentType(metrics.getPrometheusFormat());
        response.setStatus(HttpServletResponse.SC_OK);
        var w = response.getWriter();
        w.print(metrics.scrape());
        w.flush();
    }
}
