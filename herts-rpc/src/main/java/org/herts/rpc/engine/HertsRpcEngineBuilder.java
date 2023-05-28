package org.herts.rpc.engine;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.context.HertsType;
import org.herts.common.loadbalancing.LoadBalancingType;
import org.herts.common.service.HertsReactiveService;
import org.herts.common.service.HertsService;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.server.HertsMetricsServer;
import org.herts.rpc.HertsRpcServerShutdownHook;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Herts server engine builder
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcEngineBuilder {

    /**
     * Register HertsReactiveService with Interceptor.
     *
     * @param hertsReactiveService HertsReactiveService
     * @param interceptor          Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsReactiveService hertsReactiveService, @Nullable ServerInterceptor interceptor);

    /**
     * Register HertsReactiveService with Interceptor.
     *
     * @param hertsReactiveService HertsReactiveService
     * @return HertsRpcEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsReactiveService hertsReactiveService);

    /**
     * Register Herts service
     *
     * @param hertsRpcService HertsCoreService
     * @param interceptor     Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService, @Nullable ServerInterceptor interceptor);

    /**
     * Register Herts service
     *
     * @param hertsRpcService HertsCoreService
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder registerHertsRpcService(HertsService hertsRpcService);

    /**
     * Add server shutdown hook.
     *
     * @return HertsRpcEngineBuilder
     */
    HertsRpcEngineBuilder addShutdownHook(HertsRpcServerShutdownHook hook);

    /**
     * Load balancing type.
     * Default is LocalGroupRepository.
     *
     * @param loadBalancingType LoadBalancingType
     * @param connectionInfo ConnectionInfo
     * @return HertsRpcEngineBuilder
     */
    HertsRpcEngineBuilder loadBalancingType(LoadBalancingType loadBalancingType, @Nullable String connectionInfo);

    /**
     * Secure connection
     *
     * @param credentials ServerCredentials for gRPC
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder secure(ServerCredentials credentials);

    /**
     * Metrics setting
     *
     * @param metricsSetting HertsMetricsSetting
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder enableMetrics(HertsMetricsSetting metricsSetting);

    /**
     * Add custom gRPC service
     *
     * @param grpcService BindableService
     * @param hertsType   HertsType
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcEngineBuilder addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor);

    /**
     * Build HertsCoreEngine
     *
     * @return HertsCoreEngine
     */
    HertsRpcEngine build();
}
