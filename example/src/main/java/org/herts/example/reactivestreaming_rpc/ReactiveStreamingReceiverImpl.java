package org.herts.example.reactivestreaming_rpc;

import org.herts.common.logger.HertsLogger;

import java.util.Map;
import java.util.logging.Logger;

public class ReactiveStreamingReceiverImpl implements ReactiveStreamingReceiver {
    private static final Logger logger = HertsLogger.getLogger(ReactiveStreamingReceiverImpl.class.getSimpleName());

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
}
