package org.hertsstack.e2etest.reactivestreaming_rpc;

import org.hertsstack.core.annotation.HertsRpcReceiver;
import org.hertsstack.core.service.HertsReceiver;

@HertsRpcReceiver
public interface QueueTestRsReceiver extends HertsReceiver {
    void onReceived(String uniqId, String clientIf);
}
