package org.herts.e2etest.unary_rpc.client;

import org.herts.common.exception.rpc.HertsRpcErrorException;
import org.herts.e2etest.common.Constant;
import org.herts.e2etest.common.GrpcClientInterceptor;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.unary_rpc.UnaryRpcService01;
import org.herts.common.logger.HertsLogger;
import org.herts.e2etest.unary_rpc.UnaryRpcService02;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.HertsRpcClientInterceptBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnaryClient {
    private static final Logger logger = HertsLogger.getLogger(UnaryClient.class.getSimpleName());

    public static void run() {
        GrpcClientInterceptor grpcClientInterceptor = new GrpcClientInterceptor();

        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(UnaryRpcService01.class)
                .registerHertsRpcServiceInterface(UnaryRpcService02.class)
                .interceptor(HertsRpcClientInterceptBuilder.builder(grpcClientInterceptor).build())
                .connect();

        UnaryRpcService01 service_01 = client.createHertsRpcService(UnaryRpcService01.class);
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

        try {
            service_01.error01();
        } catch (HertsRpcErrorException ex) {
            logger.info(ex.getMessage() + " " + ex.getStatusCode() + " " + ex.getStatus());
        }

        try {
            service_01.error02();
        } catch (HertsRpcErrorException ex) {
            logger.info(ex.getMessage() + " " + ex.getStatusCode() + " " + ex.getStatus());
        }

        try {
            service_01.error03();
        } catch (HertsRpcErrorException ex) {
            logger.info(ex.getMessage() + " " + ex.getStatusCode() + " " + ex.getStatus());
        }

        UnaryRpcService02 service_02 = client.createHertsRpcService(UnaryRpcService02.class);
        var res0201 = service_02.hello01("ID", "Hello!");
        logger.info(res0201);
        client.getChannel().shutdown();
    }
}
