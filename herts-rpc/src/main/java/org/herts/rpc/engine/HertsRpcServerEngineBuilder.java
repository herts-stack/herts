package org.herts.rpc.engine;

import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsRpcBuildException;
import org.herts.common.logger.HertsLogger;
import org.herts.metrics.server.HertsMetricsServer;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.ServerBuilder;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.BindableService;
import io.grpc.Server;
import org.herts.rpc.HertsRpcServerShutdownHook;
import org.herts.rpc.modelx.ServerBuildInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Herts server engine builder implementation
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsRpcServerEngineBuilder implements HertsRpcServerEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsRpcServerEngineBuilder.class.getSimpleName());

    private final Map<BindableService, ServerInterceptor> services;
    private final List<HertsType> hertsTypes;
    private final GrpcServerOption option;
    private final ServerCredentials credentials;
    private final HertsMetricsServer hertsMetricsServer;
    private final HertsRpcServerShutdownHook hertsShutdownHook;

    private Server server;

    public HertsRpcServerEngineBuilder(ServerBuildInfo serverBuildInfo) {
        this.option = serverBuildInfo.getOption();
        this.credentials = serverBuildInfo.getCredentials();
        this.hertsTypes = serverBuildInfo.getHertsTypes();
        this.services = serverBuildInfo.getServices();
        this.hertsMetricsServer = serverBuildInfo.getHertsMetricsServer();
        this.hertsShutdownHook = serverBuildInfo.getHook();
    }

    public static HertsRpcServer builder() {
        return new HertsRpcServerBuilder();
    }

    public static HertsRpcServer builder(GrpcServerOption option) {
        return new HertsRpcServerBuilder(option);
    }

    @Override
    public void start() {
        try {
            if (this.option.getPort() == 8888) {
                throw new HertsRpcBuildException("Port 8888 is reserved port number for metrics");
            }

            ServerBuilder<?> serverBuilder;
            if (this.credentials != null) {
                serverBuilder = Grpc.newServerBuilderForPort(this.option.getPort(), this.credentials);
            } else {
                serverBuilder = Grpc.newServerBuilderForPort(this.option.getPort(), InsecureServerCredentials.create());
            }

            for (Map.Entry<BindableService, ServerInterceptor> service : this.services.entrySet()) {
                if (service.getValue() != null) {
                    serverBuilder = serverBuilder.addService(ServerInterceptors.intercept(service.getKey(), service.getValue()));
                } else {
                    serverBuilder = serverBuilder.addService(service.getKey());
                }
            }

            if (this.option.getMaxInboundMessageSize() > 0) {
                serverBuilder = serverBuilder.maxInboundMessageSize(this.option.getMaxInboundMessageSize());
            }
            if (this.option.getKeepaliveTimeMilliSec() != null) {
                serverBuilder = serverBuilder.keepAliveTime(this.option.getKeepaliveTimeMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getKeepaliveTimeoutMilliSec() != null) {
                serverBuilder = serverBuilder.keepAliveTimeout(this.option.getKeepaliveTimeoutMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getHandshakeTimeoutMilliSec() != null) {
                serverBuilder = serverBuilder.handshakeTimeout(this.option.getHandshakeTimeoutMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getMaxConnectionAgeMilliSec() != null) {
                serverBuilder = serverBuilder.maxConnectionAge(this.option.getMaxConnectionAgeMilliSec(), TimeUnit.MILLISECONDS);
            }
            if (this.option.getMaxConnectionIdleMilliSec() != null) {
                serverBuilder = serverBuilder.maxConnectionIdle(this.option.getMaxConnectionIdleMilliSec(), TimeUnit.MILLISECONDS);
            }

            serverBuilder = serverBuilder.maxInboundMessageSize(12000000);
            serverBuilder = serverBuilder.maxInboundMetadataSize(12000000);

            CompletableFuture.supplyAsync(() -> {
                try {
                    this.hertsMetricsServer.start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            });

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Logger _logger = HertsLogger.getLogger("ShutdownTrigger");
                        if (hertsShutdownHook != null) {
                            hertsShutdownHook.hookShutdown();
                        }
                        _logger.info("Shutdown Herts RPC server");
                        hertsMetricsServer.stop();
                        server.shutdown();
                    } finally {
                        HertsLogger.HertsLogManager.resetFinally();
                    }
                }
            });

            this.server = serverBuilder.build();
            this.server.start();
            logger.info("Started Herts RPC server. gRPC type " + this.hertsTypes.get(0) + " Port " + this.option.getPort());
            server.awaitTermination();
        } catch (Exception ex) {
            throw new HertsRpcBuildException(ex);
        }
    }

    @Override
    public void stop() {
        server.shutdown();
    }

    @Override
    public Server getServer() {
        if (this.server == null) {
            return null;
        }
        return this.server;
    }

    @Override
    public HertsType getHertCoreType() {
        if (this.hertsTypes.size() == 0) {
            return null;
        }
        return this.hertsTypes.get(0);
    }
}
