package org.herts.example.reactivesteaming;

import org.herts.core.service.HertsReactiveStreamingService;

public class ReactiveServiceImpl extends HertsReactiveStreamingService<ReactiveService, ReactiveReceiver> implements ReactiveService {
    @Override
    public String helloWorld(String value) {
        System.out.println("Received data on server: " + value);
        var clientId = getClientId();
        for (var i = 0; i < 10; i++) {
            broadcast(clientId).onReceived("send to receiver " + i);
        }
        return "hello";
    }
}
