package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface HertsEngineBuilder {
    HertsEngineBuilder addService(HertsCoreService hertsCoreService, @Nullable ServerInterceptor interceptor);

    HertsEngineBuilder secure(ServerCredentials credentials);

    HertsEngineBuilder addCustomService(BindableService grpcService, HertsCoreType hertsCoreType, @Nullable ServerInterceptor interceptor);

    HertsEngine build();

    ServerCredentials getCredentials();

    GrpcServerOption getOption();

    Map<BindableService, ServerInterceptor> getServices();

    List<HertsCoreType> getHertsCoreTypes();

    List<HertsCoreService> getHertsServices();
}
