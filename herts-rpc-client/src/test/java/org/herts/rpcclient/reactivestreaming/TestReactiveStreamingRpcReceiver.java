package org.herts.rpcclient.reactivestreaming;

import org.herts.common.annotation.HertsRpcReceiver;
import org.herts.common.reactive.HertsReceiver;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;

import java.util.List;
import java.util.Map;

@HertsRpcReceiver
public interface TestReactiveStreamingRpcReceiver extends HertsReceiver {
    void onReceiverCommand01(String id);
    void onReceiverCommand02(String id, float data01, double data02);
    void onReceiverCommand03(TestHoo hoo, TestFoo foo);
    void onReceiverCommand04(List<String> data01, Map<String, String> data02);
}
