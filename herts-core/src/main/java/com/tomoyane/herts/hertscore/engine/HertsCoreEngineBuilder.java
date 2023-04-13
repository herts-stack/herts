package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Herts server engine builder
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsCoreEngineBuilder {

    /**
     * Add Herts service
     * @param hertsCoreService HertsCoreService
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsCoreEngineBuilder addService(HertsCoreService hertsCoreService, @Nullable ServerInterceptor interceptor);

    /**
     * Secure connection
     * @param credentials ServerCredentials for gRPC
     * @return HertsCoreEngineBuilder
     */
    HertsCoreEngineBuilder secure(ServerCredentials credentials);

    /**
     * Add custom gRPC service
     * @param grpcService BindableService
     * @param hertsType HertsType
     * @param interceptor Interceptor
     * @return HertsCoreEngineBuilder
     */
    HertsCoreEngineBuilder addCustomService(BindableService grpcService, HertsType hertsType, @Nullable ServerInterceptor interceptor);

    /**
     * Build HertsCoreEngine
     * @return HertsCoreEngine
     */
    HertsCoreEngine build();

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
    List<HertsCoreService> getHertsServices();
}
