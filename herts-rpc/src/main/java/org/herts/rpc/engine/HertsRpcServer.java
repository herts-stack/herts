package org.herts.rpc.engine;

import org.herts.common.context.HertsMetricsSetting;
import org.herts.common.context.HertsType;
import org.herts.common.loadbalancing.LoadBalancingType;
import org.herts.common.reactive.HertsReactiveService;
import org.herts.common.service.HertsService;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;
import org.herts.rpc.HertsRpcServerShutdownHook;

import javax.annotation.Nullable;

/**
 * Herts server engine builder
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsRpcServer {

    /**
     * Register HertsReactiveService with Interceptor.
     *
     * @param hertsReactiveService HertsReactiveService
     * @param interceptor          Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcServer registerHertsRpcService(HertsReactiveService hertsReactiveService, @Nullable ServerInterceptor interceptor);

    /**
     * Register HertsReactiveService with Interceptor.
     *
     * @param hertsReactiveService HertsReactiveService
     * @return HertsRpcEngineBuilder
     */
    HertsRpcServer registerHertsRpcService(HertsReactiveService hertsReactiveService);

    /**
     * Register Herts service
     *
     * @param hertsRpcService HertsCoreService
     * @param interceptor     Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcServer registerHertsRpcService(HertsService hertsRpcService, @Nullable ServerInterceptor interceptor);

    /**
     * Register Herts service
     *
     * @param hertsRpcService HertsCoreService
     * @return HertsCoreEngineBuilder
     */
    HertsRpcServer registerHertsRpcService(HertsService hertsRpcService);

    /**
     * Add server shutdown hook.
     *
     * @return HertsRpcEngineBuilder
     */
    HertsRpcServer addShutdownHook(HertsRpcServerShutdownHook hook);

    /**
     * Load balancing type.
     * Default is LocalGroupRepository.
     *
     * @param loadBalancingType LoadBalancingType
     * @param connectionInfo ConnectionInfo
     * @return HertsRpcEngineBuilder
     */
    HertsRpcServer loadBalancingType(LoadBalancingType loadBalancingType, @Nullable String connectionInfo);

    /**
     * Secure connection
     *
     * @param credentials ServerCredentials for gRPC
     * @return HertsCoreEngineBuilder
     */
    HertsRpcServer secure(ServerCredentials credentials);

    /**
     * Metrics setting
     *
     * @param metricsSetting HertsMetricsSetting
     * @return HertsCoreEngineBuilder
     */
    HertsRpcServer enableMetrics(HertsMetricsSetting metricsSetting);

    /**
     * Add custom gRPC service
     *
     * @param grpcService BindableService
     * @param hertsType   HertsType
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsRpcServer addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor);

    /**
     * Build HertsCoreEngine
     *
     * @return HertsCoreEngine
     */
    HertsRpcServerEngine build();
}
