package org.herts.rpc.engine;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsReactiveService;
import org.herts.common.service.HertsService;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.server.HertsMetricsServer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Herts server engine builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcEngineBuilder {

    /**
     * Register HertsReactiveService with Interceptor.
     * @param hertsReactiveService HertsReactiveService
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsReactiveService hertsReactiveService, @Nullable ServerInterceptor interceptor);

    /**
     * Register HertsReactiveService with Interceptor.
     * @param hertsReactiveService HertsReactiveService
     * @return HertsRpcEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsReactiveService hertsReactiveService);

    /**
     * Add Herts service
     * @param hertsRpcService HertsCoreService
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService, @Nullable ServerInterceptor interceptor);

    /**
     * Add Herts service
     * @param hertsRpcService HertsCoreService
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService);

    /**
     * Secure connection
     * @param credentials ServerCredentials for gRPC
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder secure(ServerCredentials credentials);

    /**
     * Metrics setting
     * @param metricsSetting HertsMetricsSetting
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder enableMetrics(HertsMetricsSetting metricsSetting);

    /**
     * Add custom gRPC service
     * @param grpcService BindableService
     * @param hertsType HertsType
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor);

    /**
     * Build HertsCoreEngine
     * @return HertsCoreEngine
     */
    HertsRpcEngine build();

    /**
     * Get credential
     * @return ServerCredentials
     */
    ServerCredentials getCredentials();

    /**
     * Get gRPC option
     * @return GrpcServerOption
     */
    GrpcServerOption getOption();

    /**
     * Get registered services
     * @return Service information by key value
     */
    Map<BindableService, ServerInterceptor> getServices();

    /**
     * Get registered herts types
     * @return HertsType list
     */
    List<HertsType> getHertsCoreTypes();

    /**
     * Get registered herts services
     * @return HertsService list
     */
    List<HertsService> getHertsServices();

    /**
     * Get HertsMetricsServer
     * @return HertsMetricsServer
     */
    HertsMetricsServer getHertsMetricsServer();

    /**
     * HertsMetrics
     * @return HertsMetrics
     */
    HertsMetrics getHertsMetrics();
}
