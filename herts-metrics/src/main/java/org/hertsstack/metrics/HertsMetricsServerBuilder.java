package org.hertsstack.metrics;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Herts metrics server builder
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsMetricsServerBuilder {
    private Server server;
    private HttpServlet servlet;
    private HertsMetrics hertsMetrics;

    public HertsMetricsServerBuilder hertsServlet(HertsMetrics hertsMetrics) {
        this.hertsMetrics = hertsMetrics;
        this.servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                if (!request.getRequestURI().equals("/metricsz")) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                HertsMetricsServer.setMetricsResponse(hertsMetrics, response);
            }
        };
        return this;
    }

    public HertsMetricsServerBuilder server(int port) {
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(this.servlet), "/*");
        server.setHandler(context);
        this.server = server;
        return this;
    }

    public Server build() {
        return this.server;
    }

    public static HertsMetricsServerBuilder builder() {
        return new HertsMetricsServerBuilder();
    }
}
