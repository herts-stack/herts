package com.tomoyane.herts.hertsclient;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsService;
import io.grpc.ManagedChannel;

public interface HertsClient {
    String getConnectedHost();
    boolean isSecureConnection();
    ManagedChannel getChannel();
    <T extends HertsService> T createHertService(Class<T> classType);

    HertsCoreType getHertsCoreType();
}
