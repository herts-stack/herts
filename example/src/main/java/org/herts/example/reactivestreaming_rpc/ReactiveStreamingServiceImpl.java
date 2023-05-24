package org.herts.example.reactivestreaming_rpc;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsReactiveStreamingService;

import java.util.Collections;
import java.util.logging.Logger;

public class ReactiveStreamingServiceImpl extends HertsReactiveStreamingService<ReactiveStreamingService, ReactiveStreamingReceiver> implements ReactiveStreamingService {
    private static final Logger logger = HertsLogger.getLogger(ReactiveStreamingServiceImpl.class.getSimpleName());

    public ReactiveStreamingServiceImpl() {
    }

    @Override
    public void hello01() {
        logger.info("------------ ReactiveStreamingService hello01 RPC");
        var clientId = getClientId();
        broadcast(clientId).onReceivedHello01();
        broadcast(clientId).onReceivedHello02("TEST!", 9999);
        broadcast(clientId).onReceivedHello03(Collections.singletonMap("key", "value"));
    }

    @Override
    public void hello02(String id) {
        logger.info("------------ ReactiveStreamingService hello02 RPC");
        var clientId = getClientId();
        try {
            broadcast(clientId).onReceivedHello02(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
