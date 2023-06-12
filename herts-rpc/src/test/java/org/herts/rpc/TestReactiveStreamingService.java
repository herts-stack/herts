package org.herts.rpc;

import org.herts.core.service.HertsServiceReactiveStreaming;

public class TestReactiveStreamingService extends HertsServiceReactiveStreaming<TestReactiveStreamingService, TestReceiver> {
    public void foo(){
        String clientId = "";
        broadcast(clientId).test01();;
    }
}
