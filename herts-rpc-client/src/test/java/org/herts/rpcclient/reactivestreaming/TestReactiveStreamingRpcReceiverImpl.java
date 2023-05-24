package org.herts.rpcclient.reactivestreaming;

import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;

import java.util.List;
import java.util.Map;


public class TestReactiveStreamingRpcReceiverImpl implements TestReactiveStreamingRpcReceiver {
    @Override
    public void onReceiverCommand01(String id) {

    }

    @Override
    public void onReceiverCommand02(String id, float data01, double data02) {

    }

    @Override
    public void onReceiverCommand03(TestHoo hoo, TestFoo foo) {

    }

    @Override
    public void onReceiverCommand04(List<String> data01, Map<String, String> data02) {

    }
}
