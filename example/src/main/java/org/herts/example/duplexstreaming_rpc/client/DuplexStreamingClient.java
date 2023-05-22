package org.herts.example.duplexstreaming_rpc.client;

import org.herts.common.logger.HertsLogger;
import org.herts.example.common.Constant;
import org.herts.example.common.GrpcClientInterceptor;
import org.herts.example.duplexstreaming_rpc.DuplexStreamingReceiverImpl;
import org.herts.example.duplexstreaming_rpc.DuplexStreamingService;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

import java.util.logging.Logger;

public class DuplexStreamingClient {
    private static final Logger logger = HertsLogger.getLogger(DuplexStreamingClient.class.getSimpleName());

    public static void run() throws InterruptedException {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(DuplexStreamingService.class)
                .registerHertsRpcReceiver(new DuplexStreamingReceiverImpl())
                .interceptor(HertsRpcClientInterceptBuilder.builder(new GrpcClientInterceptor()).build())
                .connect();

        DuplexStreamingService service = client.createHertsRpcService(DuplexStreamingService.class);
        service.hello01();

        client.getChannel().shutdown();
    }
}
