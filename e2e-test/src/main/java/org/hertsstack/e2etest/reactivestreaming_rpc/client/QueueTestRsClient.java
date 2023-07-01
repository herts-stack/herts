package org.hertsstack.e2etest.reactivestreaming_rpc.client;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.e2etest.reactivestreaming_rpc.QueueTestRsService;
import org.hertsstack.e2etest.reactivestreaming_rpc.QueueTestRsReceiverImpl;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;

import java.time.Instant;
import java.util.UUID;

public class QueueTestRsClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(QueueTestRsClient.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = createClient();
        QueueTestRsService service = createService(client);
        for (int i = 1; i < 100000; i++) {
            String uniqId = UUID.randomUUID().toString();
            long milliseconds = timestamp();
            System.out.println("rpc_call," + uniqId + "," + milliseconds);
            service.callFoo(uniqId);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        client.getChannel().shutdown();
    }

    private static HertsRpcClient createClient() {
        return HertsRpcClientBuilder
                .builder("10.99.98.123", 8888)
                .secure(false)
                .registerHertsRpcServiceInterface(QueueTestRsService.class)
                .registerHertsRpcReceiver(new QueueTestRsReceiverImpl())
                .connect();
    }

    private static QueueTestRsService createService(HertsRpcClient client) {
        return client.createHertsRpcService(QueueTestRsService.class);
    }

    private static long timestamp() {
        Instant now = Instant.now();
        return now.toEpochMilli();
    }
}
