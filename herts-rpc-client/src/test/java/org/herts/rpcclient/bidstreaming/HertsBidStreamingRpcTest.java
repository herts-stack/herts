package org.herts.rpcclient.bidstreaming;

import io.grpc.stub.StreamObserver;
import org.herts.common.util.CollectionUtil;
import org.herts.rpc.engine.GrpcServerOption;
import org.herts.rpc.engine.HertsRpcBuilder;
import org.herts.rpc.engine.HertsRpcEngine;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;
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

    private static HertsRpcEngine engine;
    private static HertsRpcClient client;

    @BeforeAll
    static void init() throws InterruptedException {
        try {
            var t = new Thread(() -> {
                GrpcServerOption option = new GrpcServerOption();
                option.setPort(port);
                engine = HertsRpcBuilder.builder(option)
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
                .registerHertsRpcInterface(TestBidStreamingRpcService.class)
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
        var observer = clientService.test01(new StreamObserver<String>() {
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
        var observer = clientService.test02(new StreamObserver<TestHoo>() {
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
        var observer = clientService.test03(new StreamObserver<Map<String, String>>() {
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

        var uniqValues = data.values().stream().toList();
        Set<String> duplicates = CollectionUtil.findDuplicates(uniqValues);
        assertEquals(0, duplicates.size());
    }

    @Test
    public void error01() throws InterruptedException {
        TestBidStreamingRpcService clientService = client.createHertsRpcService(TestBidStreamingRpcService.class);
        final boolean[] isErr = {false};
        var observer = clientService.error01(new StreamObserver<byte[]>() {
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
        var observer = clientService.error02(new StreamObserver<byte[]>() {
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
