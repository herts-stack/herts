package com.tomoyane.herts.hertscoreclient;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import io.grpc.ManagedChannel;

public interface HertsCoreClient {
    String getConnectedHost();
    boolean isSecureConnection();
    ManagedChannel getChannel();
    <T extends HertsCoreService> T createHertCoreInterface(Class<T> classType);

    HertsCoreType getHertsCoreType();
}
