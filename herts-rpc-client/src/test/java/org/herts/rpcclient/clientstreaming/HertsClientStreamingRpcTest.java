package org.herts.rpcclient.clientstreaming;

import io.grpc.stub.StreamObserver;

import org.herts.rpc.engine.GrpcServerOption;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;
import org.herts.rpc.engine.HertsRpcServerEngine;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HertsClientStreamingRpcTest {
    private static final int port = 9999;

    private static HertsRpcServerEngine engine;
    private static HertsRpcClient client;

    @BeforeAll
    static void init() throws InterruptedException {
        try {
            Thread t = new Thread(() -> {
                GrpcServerOption option = new GrpcServerOption();
                option.setPort(port);
                engine = HertsRpcServerEngineBuilder.builder(option)
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
                .registerHertsRpcServiceInterface(TestClientStreamingRpcService.class)
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
        StreamObserver<String> observer = clientService.test01(new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
                String[] splitData = value.split("\n");
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

    @Test
    public void test02() throws InterruptedException {
        TestClientStreamingRpcService clientService = client.createHertsRpcService(TestClientStreamingRpcService.class);
        List<String> receivedData = new ArrayList<>();
        StreamObserver<TestHoo> observer = clientService.test02(new StreamObserver<TestFoo>() {
            @Override
            public void onNext(TestFoo value) {
                String[] splitData = value.getA01().split("\n");
                receivedData.addAll(Arrays.asList(splitData));
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });

        for (int i = 1; i <= 20; i++) {
            TestHoo hoo = new TestHoo();
            hoo.setA01("Hello_" + i);
            observer.onNext(hoo);
        }
        observer.onCompleted();
        Thread.sleep(2000);

        assertEquals(19, receivedData.size());
    }

    @Test
    public void test03() throws InterruptedException {
        TestClientStreamingRpcService clientService = client.createHertsRpcService(TestClientStreamingRpcService.class);
        List<String> receivedData = new ArrayList<>();
        StreamObserver<Map<String, String>> observer = clientService.test03(new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
                String[] splitData = value.split("\n");
                receivedData.addAll(Arrays.asList(splitData));
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });

        for (int i = 1; i <= 100; i++) {
            observer.onNext(Collections.singletonMap("key_" + i, "val_" + i));
        }
        observer.onCompleted();
        Thread.sleep(2000);

        assertEquals(100, receivedData.size());
    }
}
