package com.tomoyane.herts.hertshttp.engine;

import com.tomoyane.herts.hertscommon.exception.HertsHttpBuildException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.hertshttp.HertsHttpInterceptor;
import com.tomoyane.herts.hertshttp.HertsHttpInterceptorImpl;
import com.tomoyane.herts.hertshttp.HertsHttpServerBase;
import com.tomoyane.herts.hertshttp.validator.HertsHttpValidator;

import jakarta.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

public class HertsHttpEngineImpl implements HertsHttpEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsHttpEngine.class.getSimpleName());
    private static final String[] HETRS_HTTP_METHODS = new String[] { "POST", "OPTIONS" };

    private final List<HertsCoreService> hertsCoreServices;
    private final HertsHttpInterceptor interceptor;
    private final SslContextFactory sslContextFactory;
    private final int port;

    public HertsHttpEngineImpl(Builder builder) {
        this.hertsCoreServices = builder.hertsCoreServices;
        this.interceptor = builder.interceptor;
        this.sslContextFactory = builder.sslContextFactory;
        this.port = builder.port;
    }

    public static class Builder implements HertHttpEngineBuilder {
        private final List<HertsCoreService> hertsCoreServices = new ArrayList<>();
        private HertsHttpInterceptor interceptor;
        private SslContextFactory sslContextFactory;
        private int port = 8080;

        private Builder() {
        }

        public static HertHttpEngineBuilder create() {
            return new Builder();
        }

        @Override
        public HertHttpEngineBuilder setInterceptor(HertsHttpInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        @Override
        public HertHttpEngineBuilder addServiceImplementation(HertsCoreService hertsCoreService) {
            this.hertsCoreServices.add(hertsCoreService);
            return this;
        }

        @Override
        public HertHttpEngineBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        @Override
        public HertHttpEngineBuilder setSsl(SslContextFactory sslContextFactory, int port) {
            this.sslContextFactory = sslContextFactory;
            this.port = port;
            return this;
        }

        @Override
        public HertsHttpEngine build() {
            if (this.hertsCoreServices.size() == 0) {
                throw new HertsHttpBuildException("Please register HertsCoreService");
            }
            if (!HertsHttpValidator.isAllHttpType(this.hertsCoreServices)) {
                throw new HertsHttpBuildException("Please register Http HertcoreService");
            }

            var validateMsg = HertsHttpValidator.validateRegisteredServices(this.hertsCoreServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsHttpBuildException(validateMsg);
            }
            return new HertsHttpEngineImpl(this);
        }
    }

    @Override
    public void start() {
        try {
            Server server = new Server(this.port);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            List<String> endpointLogs = new ArrayList<>();
            for (HertsCoreService coreService : this.hertsCoreServices) {
                var hertsServer = new HertsHttpServerBase(coreService);
                endpointLogs.add(coreService.getClass().getSimpleName() + " endpoint.");
                for (String endpoint : hertsServer.getEndpoints()) {
                    for (String m : HETRS_HTTP_METHODS) {
                        var logM = m.equals(HETRS_HTTP_METHODS[0]) ? "[" + m + "]    " : "[" + m + "] ";
                        endpointLogs.add(logM + endpoint);
                    }
                }
                context.addServlet(new ServletHolder(hertsServer),"/*");
            }

            if (this.interceptor != null) {
                context.addFilter(new FilterHolder(
                        new HertsHttpInterceptorImpl(this.interceptor)), "/*", EnumSet.of(DispatcherType.REQUEST));
            }

            if (this.sslContextFactory != null) {
                final ServerConnector httpsConnector = new ServerConnector(server, (SslContextFactory.Server) this.sslContextFactory);
                httpsConnector.setPort(this.port);
                server.setConnectors(new Connector[] { httpsConnector });
            }

            for (String log : endpointLogs) {
                logger.info(log);
            }
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception ex) {
            throw new HertsHttpBuildException(ex);
        }
    }
}