package org.herts.example.client;

import org.herts.example.HelloRequest;
import org.herts.example.UnaryRpcRpcService01;
import org.herts.common.logger.HertsLogger;
import org.herts.example.UnaryRpcRpcService02;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnaryExample {
    private static final Logger logger = HertsLogger.getLogger(UnaryExample.class.getSimpleName());

    public static void run() throws InterruptedException {
        GrpcClientInterceptor grpcClientInterceptor = new GrpcClientInterceptor();

        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", 9000)
                .secure(false)
                .registerHertsRpcInterface(UnaryRpcRpcService01.class)
                .registerHertsRpcInterface(UnaryRpcRpcService02.class)
                .interceptor(HertsRpcClientInterceptBuilder.builder(grpcClientInterceptor).build())
                .connect();

        UnaryRpcRpcService01 service_01 = client.createHertRpcService(UnaryRpcRpcService01.class);
        var res01 = service_01.test01("TEST01", "VALUE01");
        logger.info(res01);

        var res02 = service_01.test02();
        logger.info("" + res02);

        var res03 = service_01.test03();
        logger.info(res03.get("Key"));

        var res100 = service_01.test100(new HelloRequest());
        logger.info(""  + res100);

        UnaryRpcRpcService02 service_02 = client.createHertRpcService(UnaryRpcRpcService02.class);
        var res0201 = service_02.hello01("ID", "Hello!");
        logger.info(res0201);
        client.getChannel().shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
}
