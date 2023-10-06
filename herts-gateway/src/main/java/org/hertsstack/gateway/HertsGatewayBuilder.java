package org.hertsstack.gateway;

import org.eclipse.jetty.util.ssl.SslContextFactory;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.exception.GatewayServerBuildException;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.CodeGenException;
import org.hertsstack.http.HertsHttpInterceptor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Herts Gateway builder.
 *
 * @author Herts Contributer
 */
public class HertsGatewayBuilder implements HertsGatewayServer {
    private final List<Class<?>> hertsServiceInterfaces = new ArrayList<>();
    private final Map<String, HertsHttpInterceptor> interceptorMap = new HashMap<>();
    private HertsMetricsSetting metricsSetting;
    private SslContextFactory gatewaySslContextFactory;
    private int gatewayPort = 9876;
    private int rpcPort = 9999;
    private String rpcHost = "localhost";
    private boolean isRpcSecure;

    private HertsGatewayBuilder() {
    }

    public static HertsGatewayServer builder() {
        return new HertsGatewayBuilder();
    }

    public HertsGatewayServer registerHertsRpcService(Class<?> hertsServiceInterface,  HertsHttpInterceptor interceptor) {
        if (!hertsServiceInterface.isInterface()) {
            throw new GatewayServerBuildException("Please implemented interface with @HertsRpcService Unary");
        }

        if (interceptor != null) {
            this.interceptorMap.put(hertsServiceInterface.getSimpleName(), interceptor);
        }
        this.hertsServiceInterfaces.add(hertsServiceInterface);
        return this;
    }

    public HertsGatewayServer rpcHost(String host) {
        this.rpcHost = host;
        return this;
    }

    public HertsGatewayServer gatewayPort(int port) {
        this.gatewayPort = port;
        return this;
    }
    public HertsGatewayServer rpcPort(int port) {
        this.rpcPort = port;
        return this;
    }


    public HertsGatewayServer enableMetrics(HertsMetricsSetting metricsSetting) {
        this.metricsSetting = metricsSetting;
        return this;
    }

    public HertsGatewayServer rpcSecure(boolean isRpcSecure) {
        this.isRpcSecure = isRpcSecure;
        return this;
    }

    public HertsGatewayServer gatewaySsl(org.eclipse.jetty.util.ssl.SslContextFactory sslContextFactory) {
        this.gatewaySslContextFactory = sslContextFactory;
        return this;
    }

    public HertsGatewayEngine build() {
        for (Class<?> c : this.hertsServiceInterfaces) {
            try {
                HertsRpcService annotation = c.getAnnotation(HertsRpcService.class);
                if (annotation.value() != HertsType.Unary) {
                    throw new CodeGenException("Code generation supports " + HertsType.Unary + " only");
                }
            } catch (Exception ex) {
                throw new CodeGenException("Code generation supports " + HertsType.Unary + " only");
            }
        }

        return new HertsGatewayEngineImpl(this);
    }

    public List<Class<?>> getHertsServiceInterfaces() {
        return hertsServiceInterfaces;
    }

    public Map<String, HertsHttpInterceptor> getInterceptorMap() {
        return interceptorMap;
    }

    public HertsMetricsSetting getMetricsSetting() {
        return metricsSetting;
    }

    public SslContextFactory getGatewaySslContextFactory() {
        return gatewaySslContextFactory;
    }

    public int getGatewayPort() {
        return gatewayPort;
    }

    public String getRpcHost() {
        return rpcHost;
    }

    public boolean isRpcSecure() {
        return isRpcSecure;
    }

    public int getRpcPort() {
        return rpcPort;
    }
}
