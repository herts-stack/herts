package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.common.reactive.HertsReactiveStreamingService;

public class QueueTestRsServiceImpl extends HertsReactiveStreamingService<QueueTestRsService, QueueTestRsReceiver> implements QueueTestRsService {
    @Override
    public void callFoo(String uniqId) {
        var clientId = getClientId();
        broadcast(clientId).onReceived(uniqId, clientId);
    }
}
