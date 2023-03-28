package com.tomoyane.herts.hertscoreclient;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsService;
import io.grpc.ManagedChannel;

public interface HertsCoreClient {
    String getConnectedHost();
    boolean isSecureConnection();
    ManagedChannel getChannel();
    <T extends HertsService> T createHertService(Class<T> classType);

    HertsCoreType getHertsCoreType();
}
