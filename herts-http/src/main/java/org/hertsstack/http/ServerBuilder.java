package org.hertsstack.http;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.core.exception.HttpServerBuildException;
import org.hertsstack.core.service.HertsService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Herts http server builder
 *
 * @author Herts Contributer
 */
class ServerBuilder implements HertsHttpEngineBuilder {
    private final List<HertsService> hertsRpcServices = new ArrayList<>();
    private final Map<String, HertsHttpInterceptor> interceptorMap = new HashMap<>();
    private HertsMetricsSetting metricsSetting;
    private SslContextFactory sslContextFactory;
    private int port = 8080;

    public ServerBuilder() {
    }

    @Override
    public HertsHttpEngineBuilder registerHertsHttpService(HertsService hertsRpcService, HertsHttpInterceptor interceptor) {
        if (hertsRpcService.getClass().getInterfaces().length == 0) {
            throw new HttpServerBuildException("Please implemented interface with @HertsHttp");
        }
        if (interceptor != null) {
            this.interceptorMap.put(hertsRpcService.getClass().getInterfaces()[0].getSimpleName(), interceptor);
        }
        this.hertsRpcServices.add(hertsRpcService);
        return this;
    }

    @Override
    public HertsHttpEngineBuilder registerHertsHttpService(HertsService hertsRpcService) {
        if (hertsRpcService.getClass().getInterfaces().length == 0) {
            throw new HttpServerBuildException("Please implemented interface with @HertsHttp");
        }
        this.hertsRpcServices.add(hertsRpcService);
        return this;
    }

    @Override
    public HertsHttpEngineBuilder setMetricsSetting(HertsMetricsSetting metricsSetting) {
        this.metricsSetting = metricsSetting;
        return this;
    }

    @Override
    public HertsHttpEngineBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public HertsHttpEngineBuilder setSsl(SslContextFactory sslContextFactory, int port) {
        this.sslContextFactory = sslContextFactory;
        this.port = port;
        return this;
    }

    @Override
    public HertsHttpEngine build() {
        if (this.hertsRpcServices.size() == 0) {
            throw new HttpServerBuildException("Please register HertsCoreService");
        }
        if (!HertsHttpValidator.isAllHttpTypeByService(this.hertsRpcServices)) {
            throw new HttpServerBuildException("Please register Http HertcoreService");
        }

        String validateMsg = HertsHttpValidator.validateRegisteredServices(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HttpServerBuildException(validateMsg);
        }
        return new HertsHttpServer(this);
    }

    @Override
    public List<HertsService> getHertsRpcServices() {
        return hertsRpcServices;
    }

    @Override
    public Map<String, HertsHttpInterceptor> getInterceptors() {
        return interceptorMap;
    }

    @Override
    public HertsMetricsSetting getMetricsSetting() {
        return metricsSetting;
    }

    @Override
    public SslContextFactory getSslContextFactory() {
        return sslContextFactory;
    }

    @Override
    public int getPort() {
        return port;
    }
}
