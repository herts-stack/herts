package org.hertsstack.e2etest.reactivestreaming_rpc.client;

import io.grpc.ConnectivityState;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.e2etest.reactivestreaming_rpc.IntegrationTestRsReceiverImpl;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcClientInterceptor;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.e2etest.reactivestreaming_rpc.IntegrationTestRsService;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import org.hertsstack.rpcclient.HertsRpcClientInterceptBuilder;

import java.util.Collections;

public class IntegrationTestRsClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(IntegrationTestRsClient.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(IntegrationTestRsService.class)
                .registerHertsRpcReceiver(new IntegrationTestRsReceiverImpl())
                .interceptor(HertsRpcClientInterceptBuilder.builder(new GrpcClientInterceptor()).build())
                .connect();

        IntegrationTestRsService service = client.createHertsRpcService(IntegrationTestRsService.class);
        service.hello01();
        service.hello02("id!!!!!");
        service.hello03(genRq());
        service.hello04("id", Collections.singletonMap("key", "val"));
        client.getChannel().notifyWhenStateChanged(ConnectivityState.READY, () -> {
            ConnectivityState currentState = client.getChannel().getState(true);
            if (currentState == ConnectivityState.READY) {
                logger.info("Connected");
            } else if (currentState == ConnectivityState.IDLE) {
                logger.info("Waiting for connected");
            } else if (currentState == ConnectivityState.CONNECTING) {
                logger.info("Connecting");
            } else if (currentState == ConnectivityState.TRANSIENT_FAILURE) {
                logger.info("Caught temporary error");
            } else if (currentState == ConnectivityState.SHUTDOWN) {
                logger.info("Disconnected");
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        client.getChannel().shutdown();
    }

    private static HelloRequest genRq() {
        HelloRequest rq = new HelloRequest();
        rq.setKey("test");
        return rq;
    }
}
