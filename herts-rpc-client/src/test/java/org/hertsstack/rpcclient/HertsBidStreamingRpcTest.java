package org.hertsstack.rpcclient;

import io.grpc.stub.StreamObserver;
import org.hertsstack.core.util.CollectionUtil;
import org.hertsstack.rpc.GrpcServerOption;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HertsBidStreamingRpcTest {
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
                        .registerHertsRpcService(new TestBidStreamingRpcServiceImpl())
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
                .registerHertsRpcServiceInterface(TestBidStreamingRpcService.class)
                .connect();
    }

    @AfterAll
    static void clean() {
        engine.stop();
        client.getChannel().shutdown();
    }

    @Test
    public void test01() throws InterruptedException {
        TestBidStreamingRpcService clientService = client.createHertsRpcService(TestBidStreamingRpcService.class);
        List<String> ping = new ArrayList<>();
        List<String> data = new ArrayList<>();
        StreamObserver<String> observer = clientService.test01(new StreamObserver<String>() {
            @Override
            public void onNext(String value) {
                if (value.contains("ping")) {
                    ping.add(value);
                } else {
                    String[] split = value.split("\n");
                    data.addAll(List.of(split));
                }
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });

        for (int i = 0; i < 100; i++) {
            if (i < 50) {
                observer.onNext("please_ping");
            } else {
                observer.onNext("data" + i);
            }
        }
        observer.onCompleted();
        Thread.sleep(2000);

        assertEquals(50, ping.size());
        assertEquals(50, data.size());
    }

    @Test
    public void test02() throws InterruptedException {
        TestBidStreamingRpcService clientService = client.createHertsRpcService(TestBidStreamingRpcService.class);
        List<TestHoo> data = new ArrayList<>();
        StreamObserver<TestFoo> observer = clientService.test02(new StreamObserver<TestHoo>() {
            @Override
            public void onNext(TestHoo value) {
                data.add(value);
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });

        for (int i = 0; i < 100; i++) {
            observer.onNext(new TestFoo());
        }
        observer.onCompleted();
        Thread.sleep(2000);

        assertEquals(100, data.size());
    }

    @Test
    public void test03() throws InterruptedException {
        TestBidStreamingRpcService clientService = client.createHertsRpcService(TestBidStreamingRpcService.class);
        Map<String, String> data = new HashMap<>();
        StreamObserver<Map<String, String>> observer = clientService.test03(new StreamObserver<Map<String, String>>() {
            @Override
            public void onNext(Map<String, String> value) {
                data.put(UUID.randomUUID().toString(), value.get("key"));
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });

        for (int i = 0; i < 100; i++) {
            observer.onNext(Collections.singletonMap("key", "value"));
        }
        observer.onCompleted();
        Thread.sleep(2000);

        assertEquals(100, data.size());

        List<String> uniqValues = new ArrayList<>(data.values());
        Set<String> duplicates = CollectionUtil.findDuplicates(uniqValues);
        assertEquals(0, duplicates.size());
    }

    @Test
    public void error01() throws InterruptedException {
        TestBidStreamingRpcService clientService = client.createHertsRpcService(TestBidStreamingRpcService.class);
        final boolean[] isErr = {false};
        StreamObserver<byte[]> observer = clientService.error01(new StreamObserver<byte[]>() {
            @Override
            public void onNext(byte[] value) {
            }

            @Override
            public void onError(Throwable t) {
                isErr[0] = true;
            }

            @Override
            public void onCompleted() {
            }
        });

        observer.onNext(new byte[100]);
        observer.onCompleted();
        Thread.sleep(1000);

        assertTrue(isErr[0]);
    }

    @Test
    public void error02() throws InterruptedException {
        TestBidStreamingRpcService clientService = client.createHertsRpcService(TestBidStreamingRpcService.class);
        final boolean[] isErr = {false};
        StreamObserver<byte[]> observer = clientService.error02(new StreamObserver<byte[]>() {
            @Override
            public void onNext(byte[] value) {
                throw new RuntimeException("unexpected");
            }

            @Override
            public void onError(Throwable t) {
                isErr[0] = true;
            }

            @Override
            public void onCompleted() {
            }
        });

        observer.onNext(new byte[100]);
        observer.onCompleted();
        Thread.sleep(1000);

        assertTrue(isErr[0]);
    }
}
