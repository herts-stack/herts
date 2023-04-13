package com.tomoyane.herts.httpclient;

import com.tomoyane.herts.hertscommon.exception.HertsClientBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsHttpBuildException;
import com.tomoyane.herts.hertscommon.exception.HertsServiceNotFoundException;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.httpclient.handler.HertsHttpClientHandler;
import com.tomoyane.herts.httpclient.validator.HertsHttpClientValidator;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class HertsHttpClientImpl implements HertsHttpClient {
    private final List<HertsCoreService> hertsCoreServices;
    private final String host;
    private final int serverPort;
    private final boolean isSecureConnection;

    public HertsHttpClientImpl(Builder builder) {
        this.hertsCoreServices = builder.hertsCoreServices;
        this.host = builder.host;
        this.serverPort = builder.serverPort;
        this.isSecureConnection = builder.isSecureConnection;
    }

    public static class Builder implements HertsHttpClientBuilder {
        private final List<HertsCoreService> hertsCoreServices = new ArrayList<>();
        private final String host;
        private int serverPort = 8080;
        private boolean isSecureConnection = false;

        private Builder(String connectedHost) {
            this.host = connectedHost;
        }

        public static HertsHttpClientBuilder create(String connectedHost) {
            return new Builder(connectedHost);
        }

        @Override
        public HertsHttpClientBuilder secure(boolean isSecureConnection) {
            this.isSecureConnection = isSecureConnection;
            return this;
        }

        @Override
        public HertsHttpClientBuilder port(int port) {
            this.serverPort = port;
            return this;
        }

        @Override
        public HertsHttpClientBuilder hertsImplementationService(HertsCoreService hertsCoreService) {
            this.hertsCoreServices.add(hertsCoreService);
            return this;
        }

        @Override
        public HertsHttpClient build() {
            if (this.hertsCoreServices.size() == 0 || this.host == null || this.host.isEmpty()) {
                throw new HertsClientBuildException("Please register HertsService and host");
            }
            if (!HertsHttpClientValidator.isAllHttpType(this.hertsCoreServices)) {
                throw new HertsHttpBuildException("Please register Http HertcoreService");
            }
            var validateMsg = HertsHttpClientValidator.validateRegisteredServices(this.hertsCoreServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsClientBuildException(validateMsg);
            }
            return new HertsHttpClientImpl(this);
        }
    }

    @Override
    public <T extends HertsCoreService> T createHertCoreInterface(Class<T> classType) {
        var schema = this.isSecureConnection ? "https://" + this.host + ":" + this.serverPort : "http://" + this.host + ":" + this.serverPort;

        var targetService = this.hertsCoreServices.stream()
                .filter(s -> s.getClass().getInterfaces()[0].getName().equals(classType.getName()))
                .findFirst().orElse(null);

        if (targetService == null) {
            throw new HertsServiceNotFoundException("Not found service on registered service");
        }

        var handler = new HertsHttpClientHandler(schema, targetService);
        return (T) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{ classType },
                handler);
    }
}
