package org.hertsstack.rpcclient;

import org.hertsstack.core.annotation.HertsRpcReceiver;
import org.hertsstack.core.service.HertsReceiver;

import java.util.List;
import java.util.Map;

@HertsRpcReceiver
public interface TestReactiveStreamingRpcReceiver extends HertsReceiver {
    void onReceiverCommand01(String id);
    void onReceiverCommand02(String id, float data01, double data02);
    void onReceiverCommand03(TestHoo hoo, TestFoo foo);
    void onReceiverCommand04(List<String> data01, Map<String, String> data02);
}
