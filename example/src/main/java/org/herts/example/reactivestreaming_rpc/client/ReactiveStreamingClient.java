package org.herts.example.reactivestreaming_rpc.client;

import io.grpc.ConnectivityState;
import org.herts.common.logger.HertsLogger;
import org.herts.example.common.Constant;
import org.herts.example.common.GrpcClientInterceptor;
import org.herts.example.common.HelloRequest;
import org.herts.example.reactivestreaming_rpc.ReactiveStreamingReceiverImpl;
import org.herts.example.reactivestreaming_rpc.ReactiveStreamingService;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

import java.util.Collections;
import java.util.logging.Logger;

public class ReactiveStreamingClient {
    private static final Logger logger = HertsLogger.getLogger(ReactiveStreamingClient.class.getSimpleName());

    public static void run() throws InterruptedException {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(ReactiveStreamingService.class)
                .registerHertsRpcReceiver(new ReactiveStreamingReceiverImpl())
                .interceptor(HertsRpcClientInterceptBuilder.builder(new GrpcClientInterceptor()).build())
                .connect();

        ReactiveStreamingService service = client.createHertsRpcService(ReactiveStreamingService.class);
        service.hello01();
        service.hello02(null);
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
        client.getChannel().shutdown();
    }

    private static HelloRequest genRq() {
        var rq = new HelloRequest();
        rq.setKey("test");
        return rq;
    }
}
