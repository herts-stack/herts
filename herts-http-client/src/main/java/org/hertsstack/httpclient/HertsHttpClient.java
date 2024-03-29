package org.hertsstack.httpclient;

import org.hertsstack.core.exception.ServiceNotFoundException;
import org.hertsstack.core.service.HertsService;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Herts http client
 *
 * @author Herts Contributer
 */
public class HertsHttpClient implements HertsHttpClientBase {
    private final List<Class<?>> hertsRpcServices;
    private final String host;
    private final int serverPort;
    private final boolean isSecureConnection;
    private final boolean isGateway;
    private final String schema;

    public HertsHttpClient(HertsHttpClientBuilder builder) {
        this.hertsRpcServices = builder.getHertsRpcServices();
        this.host = builder.getHost();
        this.serverPort = builder.getServerPort();
        this.isGateway = builder.isGateway();
        this.isSecureConnection = builder.isSecureConnection();
        this.schema = this.isSecureConnection ?
                "https://" + this.host + ":" + this.serverPort :
                "http://" + this.host + ":" + this.serverPort;
    }

    public static HertsHttpClientBuilder builder(String host) {
        return IBuilder.create(host);
    }

    private Class<?> createService(Class<?> classType) {
        return this.hertsRpcServices.stream()
                .filter(s -> s.getName().equals(classType.getName()))
                .findFirst()
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends HertsService> T createHertsService(Class<T> classType) {
        Class<?> targetService = createService(classType);
        if (targetService == null) {
            throw new ServiceNotFoundException("Not found service on registered service");
        }

        HertsHttpClientHandler handler = new HertsHttpClientHandler(this.schema, targetService, this.isGateway);
        return (T) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{classType},
                handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends HertsService> T createHertsService(Class<T> classType, Map<String, String> customHeaders) {
        Class<?> targetService = createService(classType);
        if (targetService == null) {
            throw new ServiceNotFoundException("Not found service on registered service");
        }
        HertsHttpClientHandler handler = new HertsHttpClientHandler(this.schema, targetService, customHeaders, this.isGateway);
        return (T) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{classType},
                handler);
    }

    @Override
    public <T extends HertsService> T recreateHertsService(Class<T> classType) {
        return createHertsService(classType);
    }

    @Override
    public <T extends HertsService> T recreateHertsService(Class<T> classType, Map<String, String> customHeader) {
        return createHertsService(classType, customHeader);
    }
}
