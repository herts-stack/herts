package org.herts.e2etest.reactivestreaming_rpc.client;

import org.herts.core.logger.HertsLogger;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsReceiverImpl;
import org.herts.e2etest.reactivestreaming_rpc.QueueTestRsService;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

public class QueueTestRsClient {
    private static final Logger logger = HertsLogger.getLogger(QueueTestRsClient.class.getSimpleName());

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
                .builder("localhost", Constant.port)
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
