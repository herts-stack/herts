package org.herts.httpclient;

import org.herts.common.exception.HertsCoreClientBuildException;
import org.herts.common.exception.HertsHttpBuildException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.service.HertsRpcService;
import org.herts.httpclient.handler.HertsHttpClientHandler;
import org.herts.httpclient.validator.HertsHttpClientValidator;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Herts http client
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpClient implements HertsHttpClientBase {
    private final List<HertsRpcService> hertsRpcServices;
    private final String host;
    private final int serverPort;
    private final boolean isSecureConnection;

    public HertsHttpClient(Builder builder) {
        this.hertsRpcServices = builder.hertsRpcServices;
        this.host = builder.host;
        this.serverPort = builder.serverPort;
        this.isSecureConnection = builder.isSecureConnection;
    }

    public static Builder builder(String host) {
        return new Builder(host);
    }

    public static class Builder implements HertsHttpClientBuilder {
        private final List<HertsRpcService> hertsRpcServices = new ArrayList<>();
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
        public HertsHttpClientBuilder hertsImplementationService(HertsRpcService hertsRpcService) {
            this.hertsRpcServices.add(hertsRpcService);
            return this;
        }

        @Override
        public HertsHttpClientBase build() {
            if (this.hertsRpcServices.size() == 0 || this.host == null || this.host.isEmpty()) {
                throw new HertsCoreClientBuildException("Please register HertsService and host");
            }
            if (!HertsHttpClientValidator.isAllHttpType(this.hertsRpcServices)) {
                throw new HertsHttpBuildException("Please register Http HertcoreService");
            }
            var validateMsg = HertsHttpClientValidator.validateRegisteredServices(this.hertsRpcServices);
            if (!validateMsg.isEmpty()) {
                throw new HertsCoreClientBuildException(validateMsg);
            }
            return new HertsHttpClient(this);
        }
    }

    @Override
    public <T extends HertsRpcService> T createHertHttpCoreInterface(Class<T> classType) {
        var schema = this.isSecureConnection ? "https://" + this.host + ":" + this.serverPort : "http://" + this.host + ":" + this.serverPort;

        var targetService = this.hertsRpcServices.stream()
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
