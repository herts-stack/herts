package org.hertsstack.e2etest.unary_rpc.client;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcClientInterceptor;
import org.hertsstack.e2etest.unary_rpc.UnaryRpcService01;
import org.hertsstack.e2etest.unary_rpc.UnaryRpcService02;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import org.hertsstack.rpcclient.HertsRpcClientInterceptBuilder;

public class AutoReconnectUnaryClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(AutoReconnectUnaryClient.class.getSimpleName());

    public static void run() {
        GrpcClientInterceptor grpcClientInterceptor = new GrpcClientInterceptor();

        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(UnaryRpcService01.class)
                .registerHertsRpcServiceInterface(UnaryRpcService02.class)
                .autoReconnection(true)
                .interceptor(HertsRpcClientInterceptBuilder.builder(grpcClientInterceptor).build())
                .connect();

        for (var i = 0; i < 10000; i++) {
            try {
                UnaryRpcService02 service_02 = client.createHertsRpcService(UnaryRpcService02.class);
                String res0201 = service_02.hello01("ID", "Hello!");
                logger.info(res0201);
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        client.getChannel().shutdown();
    }
}
