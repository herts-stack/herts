package org.herts.example.client;

import org.herts.example.HelloRequest;
import org.herts.example.UnaryRpcService01;
import org.herts.common.logger.HertsLogger;
import org.herts.example.UnaryRpcService02;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnaryExample {
    private static final Logger logger = HertsLogger.getLogger(UnaryExample.class.getSimpleName());

    public static void run() throws InterruptedException {
        GrpcClientInterceptor grpcClientInterceptor = new GrpcClientInterceptor();

        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", 9999)
                .secure(false)
                .registerHertsRpcInterface(UnaryRpcService01.class)
                .registerHertsRpcInterface(UnaryRpcService02.class)
                .interceptor(HertsRpcClientInterceptBuilder.builder(grpcClientInterceptor).build())
                .connect();

        UnaryRpcService01 service_01 = client.createHertRpcService(UnaryRpcService01.class);
        var res01 = service_01.test01("TEST01", "VALUE01");
        logger.info(res01);

        var res02 = service_01.test02();
        logger.info("" + res02);

        var res03 = service_01.test03();
        logger.info(res03.get("Key"));

        var res100 = service_01.test100(new HelloRequest());
        logger.info(""  + res100);

        var res101 = service_01.test101(Collections.singletonMap("key", "map_val"), Collections.singletonList("hello"));
        logger.info(""  + res101);

        service_01.test102();

        UnaryRpcService02 service_02 = client.createHertRpcService(UnaryRpcService02.class);
        var res0201 = service_02.hello01("ID", "Hello!");
        logger.info(res0201);
        client.getChannel().shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
}
