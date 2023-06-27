package herts.org.broker;

import org.herts.core.annotation.HertsRpcReceiver;
import org.herts.core.service.HertsReceiver;

@HertsRpcReceiver
public interface TestReactiveStreamingRpcReceiver extends HertsReceiver {
    void onReceiverCommand01(String id);
}
