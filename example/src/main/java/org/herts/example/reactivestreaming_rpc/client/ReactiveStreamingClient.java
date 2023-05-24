package org.herts.example.reactivestreaming_rpc.client;

import org.herts.common.logger.HertsLogger;
import org.herts.example.common.Constant;
import org.herts.example.common.GrpcClientInterceptor;
import org.herts.example.reactivestreaming_rpc.ReactiveStreamingReceiverImpl;
import org.herts.example.reactivestreaming_rpc.ReactiveStreamingService;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

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
        client.getChannel().shutdown();
    }
}
