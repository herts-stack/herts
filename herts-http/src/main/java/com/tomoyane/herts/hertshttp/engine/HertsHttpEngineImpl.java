package com.tomoyane.herts.hertshttp.engine;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.hertshttp.HertsHttpServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HertsHttpEngineImpl implements HertsHttpEngine {

    private final HertsCoreService hertsCoreService;
    public HertsHttpEngineImpl(HertsCoreService hertsCoreService) {
        this.hertsCoreService = hertsCoreService;
    }

    @Override
    public void start() {
        try {
            Server server = new Server(8080);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            context.addServlet(new ServletHolder(new HertsHttpServer(this.hertsCoreService)),"/*");
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
