package org.hertsstack.e2etest.unary_rpc.client;

import org.hertsstack.core.exception.rpc.RpcErrorException;
import org.hertsstack.e2etest.unary_rpc.UnaryRpcService01;
import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.GrpcClientInterceptor;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.e2etest.unary_rpc.UnaryRpcService02;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import org.hertsstack.rpcclient.HertsRpcClientInterceptBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UnaryClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(UnaryClient.class.getSimpleName());

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
        String res01 = service_01.test01("TEST01", "VALUE01");
        logger.info(res01);

        boolean res02 = service_01.test02();
        logger.info("" + res02);

        Map<String, String> res03 = service_01.test03();
        logger.info(res03.get("Key"));

        boolean res100 = service_01.test100(new HelloRequest());
        logger.info(""  + res100);

        List<String> res101 = service_01.test101(Collections.singletonMap("key", "map_val"), Collections.singletonList("hello"));
        logger.info(""  + res101);

        service_01.test102();

        try {
            service_01.error01();
        } catch (RpcErrorException ex) {
            logger.info(ex.getMessage() + " " + ex.getStatusCode() + " " + ex.getStatus());
        }

        try {
            service_01.error02();
        } catch (RpcErrorException ex) {
            logger.info(ex.getMessage() + " " + ex.getStatusCode() + " " + ex.getStatus());
        }

        try {
            service_01.error03();
        } catch (RpcErrorException ex) {
            logger.info(ex.getMessage() + " " + ex.getStatusCode() + " " + ex.getStatus());
        }

        UnaryRpcService02 service_02 = client.createHertsRpcService(UnaryRpcService02.class);
        String res0201 = service_02.hello01("ID", "Hello!");
        logger.info(res0201);
        client.getChannel().shutdown();
    }
}