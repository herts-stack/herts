package org.hertsstack.e2etest.reactivestreaming_rpc;

import org.hertsstack.core.annotation.HertsRpcReceiver;
import org.hertsstack.core.service.HertsReceiver;
import org.hertsstack.e2etest.common.HelloRequest;

import java.util.List;
import java.util.Map;

@HertsRpcReceiver
public interface IntegrationTestRsReceiver extends HertsReceiver {
    void onReceivedHello01();

    void onReceivedHello02(String a, int b);

    void onReceivedHello03(Map<String, String> a);

    void onReceivedHello04(HelloRequest req);

    void onReceivedHello05(List<String> data01, Map<String, String> data02);
}
