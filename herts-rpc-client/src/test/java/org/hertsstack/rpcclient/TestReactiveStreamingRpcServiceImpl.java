package org.hertsstack.rpcclient;

import org.hertsstack.core.service.HertsServiceReactiveStreaming;

import java.util.Map;

public class TestReactiveStreamingRpcServiceImpl
        extends HertsServiceReactiveStreaming<TestReactiveStreamingRpcService, TestReactiveStreamingRpcReceiver>
        implements TestReactiveStreamingRpcService {

    @Override
    public String test01() {
        String clientId = getClientId();
        broadcast(clientId).onReceiverCommand01(clientId);
        return clientId;
    }

    @Override
    public boolean test02(int id, String data) {
        String clientId = getClientId();
        broadcast(clientId).onReceiverCommand02(clientId, 01f, 0.999);
        return true;
    }

    @Override
    public Map<String, String> test03(TestHoo hoo) {
        String clientId = getClientId();
        broadcast(clientId).onReceiverCommand03(hoo, new TestFoo());
        return hoo.getD01();
    }

    @Override
    public TestFoo test04(String id, Map<String, String> data01) {
        String clientId = getClientId();
        broadcast(clientId).onReceiverCommand04(null, null);
        TestFoo foo = new TestFoo();
        foo.setA01("OK!");
        return foo;
    }
}
