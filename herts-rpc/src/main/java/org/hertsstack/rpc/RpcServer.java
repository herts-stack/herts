package org.hertsstack.rpc;

import org.hertsstack.broker.ReactiveBroker;
import org.hertsstack.core.context.HertsMetricsSetting;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsReactiveService;
import org.hertsstack.core.service.HertsService;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;

import javax.annotation.Nullable;

/**
 * Herts server engine builder
 *
 * @author Herts Contributer
 */
public interface RpcServer {

    /**
     * Register HertsReactiveService with Interceptor.
     *
     * @param hertsReactiveService HertsReactiveService
     * @param interceptor          Interceptor
     * @return HertsCoreEngineBuilder
     */
    RpcServer registerHertsReactiveRpcService(HertsReactiveService hertsReactiveService, @Nullable ServerInterceptor interceptor);

    /**
     * Register HertsReactiveService with Interceptor.
     *
     * @param hertsReactiveService HertsReactiveService
     * @return HertsRpcEngineBuilder
     */
    RpcServer registerHertsReactiveRpcService(HertsReactiveService hertsReactiveService);

    /**
     * Register Herts service
     *
     * @param hertsRpcService HertsCoreService
     * @param interceptor     Interceptor
     * @return HertsCoreEngineBuilder
     */
    RpcServer registerHertsRpcService(HertsService hertsRpcService, @Nullable ServerInterceptor interceptor);

    /**
     * Register Herts service
     *
     * @param hertsRpcService HertsCoreService
     * @return HertsCoreEngineBuilder
     */
    RpcServer registerHertsRpcService(HertsService hertsRpcService);

    /**
     * Add server shutdown hook.
     *
     * @param hook HertsRpcServerShutdownHook
     * @return HertsRpcEngineBuilder
     */
    RpcServer addShutdownHook(HertsRpcServerShutdownHook hook);

    /**
     * Load balancing type.
     * Default is LocalGroupRepository.
     *
     * @param broker ReactiveBroker
     * @return HertsRpcEngineBuilder
     */
    RpcServer loadBalancingBroker(ReactiveBroker broker);

    /**
     * Secure connection
     *
     * @param credentials ServerCredentials for gRPC
     * @return HertsCoreEngineBuilder
     */
    RpcServer secure(ServerCredentials credentials);

    /**
     * Metrics setting
     *
     * @param metricsSetting HertsMetricsSetting
     * @return HertsCoreEngineBuilder
     */
    RpcServer enableMetrics(HertsMetricsSetting metricsSetting);

    /**
     * Add custom gRPC service
     *
     * @param grpcService BindableService
     * @param hertsType   HertsType
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    RpcServer addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor);

    /**
     * Build HertsCoreEngine
     *
     * @return HertsCoreEngine
     */
    HertsRpcServerEngine build();
}
