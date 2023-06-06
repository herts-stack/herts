package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.common.logger.HertsLogger;
import org.herts.common.reactive.HertsReactiveStreamingService;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.common.HelloResponse01;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class IntegrationTestRsServiceImpl extends HertsReactiveStreamingService<IntegrationTestRsService, IntegrationTestRsReceiver> implements IntegrationTestRsService {
    private static final Logger logger = HertsLogger.getLogger(IntegrationTestRsServiceImpl.class.getSimpleName());

    public IntegrationTestRsServiceImpl() {
    }

    @Override
    public void hello01() {
        logger.info("------------ ReactiveStreamingService hello01 RPC broadcast onReceivedHello01, 02, 03");
        String clientId = getClientId();
        broadcast(clientId).onReceivedHello01();
        broadcast(clientId).onReceivedHello02("TEST!", 9999);
        broadcast(clientId).onReceivedHello03(Collections.singletonMap("key", "value"));
    }

    @Override
    public void hello02(String id) {
        logger.info("------------ ReactiveStreamingService hello02 RPC broadcast onReceivedHello02");
        String clientId = getClientId();
        try {
            broadcast(clientId).onReceivedHello02(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> hello03(HelloRequest req) {
        logger.info("------------ ReactiveStreamingService hello03 RPC broadcast onReceivedHello04");
        broadcast(getClientId()).onReceivedHello04(req);
        return Collections.singletonMap("key", req.getKey());
    }

    @Override
    public HelloResponse01 hello04(String id, Map<String, String> data01) {
        logger.info("------------ ReactiveStreamingService hello04 RPC broadcast onReceivedHello05");
        HelloResponse01 res = new HelloResponse01();
        res.setCode(999);
        res.setTest("test");
        broadcast(getClientId()).onReceivedHello05(null, null);
        return res;
    }
}
