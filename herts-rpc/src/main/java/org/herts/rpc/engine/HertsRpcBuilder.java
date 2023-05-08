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

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HertsRpcBuilder implements HertsRpcEngine {
    private static final Logger logger = HertsLogger.getLogger(HertsRpcBuilder.class.getSimpleName());

    private final Map<BindableService, ServerInterceptor> services;
    private final List<HertsType> hertsTypes;
    private final GrpcServerOption option;
    private final ServerCredentials credentials;
    private final HertsMetricsServer hertsMetricsServer;

    private Server server;

    public HertsRpcBuilder(HertsRpcEngineBuilder builder) {
        this.option = builder.getOption();
        this.credentials = builder.getCredentials();
        this.hertsTypes = builder.getHertsCoreTypes();
        this.services = builder.getServices();
        this.hertsMetricsServer = builder.getHertsMetricsServer();
    }

    public static HertsRpcEngineBuilder builder() {
        return new org.herts.rpc.engine.ServerBuilder();
    }

    public static HertsRpcEngineBuilder builder(GrpcServerOption option) {
        return new org.herts.rpc.engine.ServerBuilder(option);
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
                serverBuilder = serverBuilder.addService(service.getKey());
                if (service.getValue() != null) {
                    serverBuilder = serverBuilder.addService(ServerInterceptors.intercept(service.getKey(), service.getValue()));
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

            // TODO: Graceful shutdown
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                try {
                    this.hertsMetricsServer.start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            });

            this.server = serverBuilder.build();
            this.server.start();
            logger.info("Started Herts server. gRPC type " + this.hertsTypes.get(0) + " Port " + this.option.getPort());
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
