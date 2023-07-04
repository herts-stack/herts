package org.hertsstack.e2etest.serverstreaming_rpc.client;

import org.hertsstack.e2etest.common.Constant;
import org.hertsstack.e2etest.common.HelloRequest;
import org.hertsstack.e2etest.common.HelloResponse01;
import org.hertsstack.e2etest.serverstreaming_rpc.ServerStreamingRpcService;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.e2etest.common.HelloResponse02;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import io.grpc.stub.StreamObserver;

public class ServerStreamingClient {
    private static final java.util.logging.Logger logger = Logging.getLogger(ServerStreamingClient.class.getSimpleName());

    public static void run() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost", Constant.port)
                .secure(false)
                .registerHertsRpcServiceInterface(ServerStreamingRpcService.class)
                .connect();

        ServerStreamingRpcService service = client.createHertsRpcService(ServerStreamingRpcService.class);
        HelloRequest req = new HelloRequest();
        req.setNumber(7777);
        service.test05("ABC_id", req, new StreamObserver<HelloResponse01>() {
            @Override
            public void onNext(HelloResponse01 res) {
                logger.info(String.format("Got message at %d, %d", res.getCode(), res.getTimestamp()));
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                logger.info("ERROR");
            }

            @Override
            public void onCompleted() {
                logger.info("onCompleted");
            }
        });

        service.test01("id", "no", new StreamObserver<HelloResponse02>() {
            @Override
            public void onNext(HelloResponse02 value) {
                logger.info("recevice");
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                logger.info("done");
            }
        });

        service.test04("id", new StreamObserver<Object>() {
            @Override
            public void onNext(Object value) {
                logger.info("" + value);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                logger.info("done");
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
