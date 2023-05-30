package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.common.annotation.HertsRpcReceiver;
import org.herts.common.reactive.HertsReceiver;

@HertsRpcReceiver
public interface QueueTestRsReceiver extends HertsReceiver {
    void onReceived(String uniqId, String clientIf);
}
