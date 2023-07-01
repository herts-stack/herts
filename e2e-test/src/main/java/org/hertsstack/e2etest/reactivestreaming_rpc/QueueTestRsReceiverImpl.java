package org.hertsstack.e2etest.reactivestreaming_rpc;

import java.time.Instant;

public class QueueTestRsReceiverImpl implements QueueTestRsReceiver {
    @Override
    public void onReceived(String uniqId, String clientId) {
        Instant now = Instant.now();
        long milliseconds = now.toEpochMilli();
        System.out.println("receive," + uniqId + "," + clientId + "," + milliseconds);
    }
}
