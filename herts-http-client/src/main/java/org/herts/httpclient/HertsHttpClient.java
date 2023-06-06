package org.herts.httpclient;

import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.service.HertsService;
import org.herts.httpclient.handler.HertsHttpClientHandler;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Herts http client
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpClient implements HertsHttpClientBase {
    private final List<Class<?>> hertsRpcServices;
    private final String host;
    private final int serverPort;
    private final boolean isSecureConnection;
    private final String schema;

    public HertsHttpClient(HertsHttpClientBuilder builder) {
        this.hertsRpcServices = builder.getHertsRpcServices();
        this.host = builder.getHost();
        this.serverPort = builder.getServerPort();
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
    public <T extends HertsService> T createHertsService(Class<T> classType) {
        Class<?> targetService = createService(classType);
        if (targetService == null) {
            throw new HertsServiceNotFoundException("Not found service on registered service");
        }

        HertsHttpClientHandler handler = new HertsHttpClientHandler(schema, targetService);
        return (T) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{ classType },
                handler);
    }

    @Override
    public <T extends HertsService> T createHertsService(Class<T> classType, Map<String, String> customHeaders) {
        Class<?> targetService = createService(classType);
        if (targetService == null) {
            throw new HertsServiceNotFoundException("Not found service on registered service");
        }
        HertsHttpClientHandler handler = new HertsHttpClientHandler(schema, targetService, customHeaders);
        return (T) Proxy.newProxyInstance(
                classType.getClassLoader(),
                new Class<?>[]{ classType },
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
