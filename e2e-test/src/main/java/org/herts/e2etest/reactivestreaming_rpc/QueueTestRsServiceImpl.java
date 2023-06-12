package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.core.service.HertsServiceReactiveStreaming;

public class QueueTestRsServiceImpl extends HertsServiceReactiveStreaming<QueueTestRsService, QueueTestRsReceiver> implements QueueTestRsService {
    @Override
    public void callFoo(String uniqId) {
        String clientId = getClientId();
        broadcast(clientId).onReceived(uniqId, clientId);
    }
}
