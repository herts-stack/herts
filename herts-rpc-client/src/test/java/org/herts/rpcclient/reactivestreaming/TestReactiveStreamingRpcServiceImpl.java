package org.herts.rpcclient.reactivestreaming;

import org.herts.common.service.HertsReactiveStreamingService;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;

import java.util.Map;

public class TestReactiveStreamingRpcServiceImpl
        extends HertsReactiveStreamingService<TestReactiveStreamingRpcService, TestReactiveStreamingRpcReceiver>
        implements TestReactiveStreamingRpcService {

    @Override
    public String test01() {
        var clientId = getClientId();
        broadcast(clientId).onReceiverCommand01(clientId);
        return clientId;
    }

    @Override
    public boolean test02(int id, String data) {
        var clientId = getClientId();
        broadcast(clientId).onReceiverCommand02(clientId, 01f, 0.999);
        return true;
    }

    @Override
    public Map<String, String> test03(TestHoo hoo) {
        var clientId = getClientId();
        broadcast(clientId).onReceiverCommand03(hoo, new TestFoo());
        return hoo.getD01();
    }

    @Override
    public TestFoo test04(String id, Map<String, String> data01) {
        var clientId = getClientId();
        broadcast(clientId).onReceiverCommand04(null, null);
        var foo = new TestFoo();
        foo.setA01("OK!");
        return foo;
    }
}
