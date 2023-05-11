package org.herts.rpcclient.clientstreaming;

import io.grpc.stub.StreamObserver;

import org.herts.rpc.engine.GrpcServerOption;
import org.herts.rpc.engine.HertsRpcBuilder;
import org.herts.rpc.engine.HertsRpcEngine;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HertsClientStreamingRpcTest {
    private static final int port = 9999;

    private static HertsRpcEngine engine;
    private static HertsRpcClient client;

    @BeforeAll
    static void init() throws InterruptedException {
        try {
            var t = new Thread(() -> {
                GrpcServerOption option = new GrpcServerOption();
                option.setPort(port);
                engine = HertsRpcBuilder.builder(option)
                        .registerHertsRpcService(new TestClientStreamingRpcServiceImpl())
                        .build();
                engine.start();
            });
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Thread.sleep(1000);

        client = HertsRpcClientBuilder
                .builder("localhost", port)
                .secure(false)
                .registerHertsRpcInterface(TestClientStreamingRpcService.class)
                .connect();
    }

    @AfterAll
    static void clean() {
        engine.stop();
        client.getChannel().shutdown();
    }

    @Test
    public void test01() throws InterruptedException {
        TestClientStreamingRpcService clientService = client.createHertsRpcService(TestClientStreamingRpcService.class);
        List<String> receivedData = new ArrayList<>();
        var observer = clientService.test01(new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
                var splitData = value.split("\n");
                receivedData.addAll(Arrays.asList(splitData));
            }

            @Override
            public void onError(Throwable t) {
                receivedData.add("ERRRR");
            }

            @Override
            public void onCompleted() {
            }
        });

        for (int i = 1; i <= 10; i++) {
            observer.onNext("Hello" + i);
        }
        observer.onCompleted();
        Thread.sleep(2000);

        assertEquals(10, receivedData.size());
    }
}
