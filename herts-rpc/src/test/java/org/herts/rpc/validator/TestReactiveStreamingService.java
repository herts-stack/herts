package org.herts.rpc.validator;

import org.herts.common.reactive.HertsReactiveStreamingService;

public class TestReactiveStreamingService extends HertsReactiveStreamingService<TestReactiveStreamingService, TestReceiver> {
    public void foo(){
        var clientId = "";
        broadcast(clientId).test01();;
    }
}
