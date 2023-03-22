package com.tomoyane.herts.hertsclient;

import com.tomoyane.herts.hertscore.service.HertsService;
import io.grpc.ManagedChannel;

public interface HertsClient {
    String getConnectedHost();
    boolean isSecureConnection();
    ManagedChannel getChannel();
    HertsService createHertService(Class<?> classType);
}
