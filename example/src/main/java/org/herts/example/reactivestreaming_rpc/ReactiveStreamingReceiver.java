package org.herts.example.reactivestreaming_rpc;

import org.herts.common.annotation.HertsRpcReceiver;
import org.herts.common.reactive.HertsReceiver;
import org.herts.example.common.HelloRequest;

import java.util.List;
import java.util.Map;

@HertsRpcReceiver
public interface ReactiveStreamingReceiver extends HertsReceiver {
    void onReceivedHello01();

    void onReceivedHello02(String a, int b);

    void onReceivedHello03(Map<String, String> a);

    void onReceivedHello04(HelloRequest req);

    void onReceivedHello05(List<String> data01, Map<String, String> data02);
}
