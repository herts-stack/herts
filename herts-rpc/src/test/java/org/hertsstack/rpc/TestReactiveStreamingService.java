package org.hertsstack.rpc;

import org.hertsstack.core.service.HertsServiceReactiveStreaming;

public class TestReactiveStreamingService extends HertsServiceReactiveStreaming<TestReactiveStreamingService, TestReceiver> {
    public void foo(){
        String clientId = "";
        broadcast(clientId).test01();;
    }
}
