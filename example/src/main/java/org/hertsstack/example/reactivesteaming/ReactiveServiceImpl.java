package org.hertsstack.example.reactivesteaming;

import org.hertsstack.core.service.HertsServiceReactiveStreaming;
import org.hertsstack.example.commonmodel.Hoo;

public class ReactiveServiceImpl extends HertsServiceReactiveStreaming<ReactiveService, ReactiveReceiver> implements ReactiveService {
    @Override
    public String helloWorld(String value) {
        System.out.println("Received data on server: " + value);
        var clientId = getClientId();
        for (var i = 0; i < 10; i++) {
            broadcast(clientId).onReceived("send to receiver " + i);
        }
        return "hello";
    }

    @Override
    public Hoo getHoo() {
        return null;
    }
}
