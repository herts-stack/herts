package org.herts.rpc.modelx;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;
import org.herts.common.context.HertsType;
import org.herts.metrics.server.HertsMetricsServer;
import org.herts.rpc.HertsRpcServerShutdownHook;
import org.herts.rpc.engine.GrpcServerOption;

import java.util.List;
import java.util.Map;

/**
 * Herts build info
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class ServerBuildInfo {
    private Map<BindableService, ServerInterceptor> services;
    private List<HertsType> hertsTypes;
    private GrpcServerOption option;
    private ServerCredentials credentials;
    private HertsMetricsServer hertsMetricsServer;
    private HertsRpcServerShutdownHook hook;

    public ServerBuildInfo(Map<BindableService, ServerInterceptor> services, List<HertsType> hertsTypes,
                           GrpcServerOption option, ServerCredentials credentials,
                           HertsMetricsServer hertsMetricsServer, HertsRpcServerShutdownHook hook) {
        this.services = services;
        this.hertsTypes = hertsTypes;
        this.option = option;
        this.credentials = credentials;
        this.hertsMetricsServer = hertsMetricsServer;
        this.hook = hook;
    }

    public Map<BindableService, ServerInterceptor> getServices() {
        return services;
    }

    public void setServices(Map<BindableService, ServerInterceptor> services) {
        this.services = services;
    }

    public List<HertsType> getHertsTypes() {
        return hertsTypes;
    }

    public void setHertsTypes(List<HertsType> hertsTypes) {
        this.hertsTypes = hertsTypes;
    }

    public GrpcServerOption getOption() {
        return option;
    }

    public void setOption(GrpcServerOption option) {
        this.option = option;
    }

    public ServerCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(ServerCredentials credentials) {
        this.credentials = credentials;
    }

    public HertsMetricsServer getHertsMetricsServer() {
        return hertsMetricsServer;
    }

    public void setHertsMetricsServer(HertsMetricsServer hertsMetricsServer) {
        this.hertsMetricsServer = hertsMetricsServer;
    }

    public HertsRpcServerShutdownHook getHook() {
        return hook;
    }

    public void setHook(HertsRpcServerShutdownHook hook) {
        this.hook = hook;
    }
}
