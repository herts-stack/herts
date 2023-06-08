package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.core.annotation.HertsRpcReceiver;
import org.herts.core.service.HertsReceiver;

@HertsRpcReceiver
public interface QueueTestRsReceiver extends HertsReceiver {
    void onReceived(String uniqId, String clientIf);
}
