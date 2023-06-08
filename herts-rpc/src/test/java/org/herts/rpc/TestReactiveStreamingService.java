package org.herts.rpc;

import org.herts.core.service.HertsReactiveStreamingService;

public class TestReactiveStreamingService extends HertsReactiveStreamingService<TestReactiveStreamingService, TestReceiver> {
    public void foo(){
        String clientId = "";
        broadcast(clientId).test01();;
    }
}
