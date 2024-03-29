package org.hertsstack.httpclient;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.HttpClientBuildException;
import org.hertsstack.core.exception.HttpServerBuildException;

import java.util.ArrayList;
import java.util.List;

class IBuilder implements HertsHttpClientBuilder {
    private final List<Class<?>> hertsRpcServices = new ArrayList<>();
    private final String host;
    private int serverPort = 8080;
    private boolean isSecureConnection = false;
    private boolean isGateway = false;

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
    public <T> HertsHttpClientBuilder registerHertsService(Class<T> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new HttpServerBuildException("Please register Interface with extends HertsRpcService");
        }
        this.hertsRpcServices.add(interfaceClass);
        return this;
    }

    @Override
    public HertsHttpClientBuilder gatewayApi(boolean isGateway) {
        this.isGateway = isGateway;
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
    public boolean isGateway() {
        return this.isGateway;
    }

    @Override
    public HertsHttpClient build() {
        if (this.hertsRpcServices.size() == 0 || this.host == null || this.host.isEmpty()) {
            throw new HttpServerBuildException("Please register HertsService and host");
        }

        List<HertsType> hertsTypes = new ArrayList<>();
        for (Class<?> c : this.hertsRpcServices) {
            try {
                if (!this.isGateway) {
                    HertsHttp annotation = c.getAnnotation(HertsHttp.class);
                    hertsTypes.add(annotation.value());
                } else {
                    HertsRpcService annotation = c.getAnnotation(HertsRpcService.class);
                    if (annotation.value() != HertsType.Unary) {
                        throw new HttpClientBuildException("Code generation supports " + HertsType.Unary + " only if use Gateway");
                    }
                    hertsTypes.add(HertsType.Http);
                }
            } catch (HttpClientBuildException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new HttpClientBuildException("Could not find @HertsHttp annotation in " + c.getName(), ex);
            }
        }

        if (!HertsHttpClientValidator.isAllHttpType(hertsTypes)) {
            throw new HttpServerBuildException("Please register Http HertService");
        }
        String validateMsg = HertsHttpClientValidator.validateMethod(this.hertsRpcServices);
        if (!validateMsg.isEmpty()) {
            throw new HttpServerBuildException(validateMsg);
        }
        return new HertsHttpClient(this);
    }
}
