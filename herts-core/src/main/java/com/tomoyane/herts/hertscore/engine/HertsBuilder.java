package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsService;

import io.grpc.BindableService;
import io.grpc.ServerCredentials;
import io.grpc.ServerInterceptor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface HertsBuilder {
    HertsBuilder addService(HertsService hertsService, @Nullable ServerInterceptor interceptor);

    HertsBuilder secure(ServerCredentials credentials);

    HertsBuilder addCustomService(BindableService grpcService, HertsCoreType hertsCoreType, @Nullable ServerInterceptor interceptor);

    HertsEngine build();

    ServerCredentials getCredentials();

    int getPort();

    Map<BindableService, ServerInterceptor> getServices();

    List<HertsCoreType> getHertsCoreTypes();
}
