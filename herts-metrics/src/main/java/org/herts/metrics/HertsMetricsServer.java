package org.herts.metrics;

import org.eclipse.jetty.server.Server;
import org.herts.core.logger.HertsLogger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

    public void stop() {
        if (this.server == null) {
            return;
        }
        try {
            this.server.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        PrintWriter w = response.getWriter();
        w.print(metrics.scrape());
        w.flush();
    }
}
