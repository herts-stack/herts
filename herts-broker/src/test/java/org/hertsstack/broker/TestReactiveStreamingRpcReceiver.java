package org.hertsstack.broker;

import org.hertsstack.core.annotation.HertsRpcReceiver;
import org.hertsstack.core.service.HertsReceiver;

@HertsRpcReceiver
public interface TestReactiveStreamingRpcReceiver extends HertsReceiver {
    void onReceiverCommand01(String id);
}
