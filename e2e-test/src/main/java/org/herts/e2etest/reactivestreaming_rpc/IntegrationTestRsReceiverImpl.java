package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.core.logger.Logging;
import org.herts.e2etest.common.HelloRequest;

import java.util.List;
import java.util.Map;

public class IntegrationTestRsReceiverImpl implements IntegrationTestRsReceiver {
    private static final java.util.logging.Logger logger = Logging.getLogger(IntegrationTestRsReceiverImpl.class.getSimpleName());

    @Override
    public void onReceivedHello01() {
        logger.info("------------- onReceivedHello01 event");
    }

    @Override
    public void onReceivedHello02(String a, int b) {
        logger.info("------------- onReceivedHello02 event");
        logger.info("a=" + a + ", b=" + b);
    }

    @Override
    public void onReceivedHello03(Map<String, String> a) {
        logger.info("------------- onReceivedHello03 event");
        for (Map.Entry<String, String> data : a.entrySet()) {
            logger.info("key=" + data.getKey() + ", value-" + data.getValue());
        }
    }

    @Override
    public void onReceivedHello04(HelloRequest req) {
        logger.info("------------- onReceivedHello04 event");
    }

    @Override
    public void onReceivedHello05(List<String> data01, Map<String, String> data02) {
        logger.info("------------- onReceivedHello05 event");
    }
}
