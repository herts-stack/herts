package org.herts.httpclient;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsHttpBuildException;
import org.herts.common.exception.HertsRpcClientBuildException;
import org.herts.httpclient.validator.HertsHttpClientValidator;

import java.util.ArrayList;
import java.util.List;

public class IBuilder implements HertsHttpClientBuilder {
    private final List<Class<?>> hertsRpcServices = new ArrayList<>();
    private final String host;
    private int serverPort = 8080;
    private boolean isSecureConnection = false;

    private IBuilder(String connectedHost) {
        this.host = connectedHost;
    }

    public static HertsHttpClientBuilder create(String connectedHost) {
        return new IBuilder(connectedHost);
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
    public <T> HertsHttpClientBuilder registerHertService(Class<T> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new HertsRpcClientBuildException("Please register Interface with extends HertsRpcService");
        }
        this.hertsRpcServices.add(interfaceClass);
        return this;
    }

    @Override
    public List<Class<?>> getHertsRpcServices() {
        return hertsRpcServices;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public boolean isSecureConnection() {
        return isSecureConnection;
    }

    @Override
    public HertsHttpClientBase build() {
        if (this.hertsRpcServices.size() == 0 || this.host == null || this.host.isEmpty()) {
            throw new HertsRpcClientBuildException("Please register HertsService and host");
        }

        List<HertsType> hertsTypes = new ArrayList<>();
        for (Class<?> c : this.hertsRpcServices) {
            try {
                var annotation = c.getAnnotation(HertsHttp.class);
                hertsTypes.add(annotation.value());
            } catch (Exception ex) {
                throw new HertsRpcClientBuildException("Could not find @HertsHttp annotation in " + c.getName(), ex);
            }
        }

        if (!HertsHttpClientValidator.isAllHttpType(hertsTypes)) {
            throw new HertsHttpBuildException("Please register Http HertService");
        }
        var validateMsg = HertsHttpClientValidator.validateMethod(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HertsRpcClientBuildException(validateMsg);
        }
        return new HertsHttpClient(this);
    }
}
