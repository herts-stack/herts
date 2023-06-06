package org.herts.rpcclient.serverstreaming;

import io.grpc.stub.StreamObserver;
import org.herts.rpc.engine.GrpcServerOption;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;
import org.herts.rpc.engine.HertsRpcServerEngine;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.TestHoo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HertsServerStreamingRpcTest {
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
                        .registerHertsRpcService(new TestServerStreamingRpcServiceImpl())
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
                .registerHertsRpcServiceInterface(TestServerStreamingRpcService.class)
                .connect();
    }

    @AfterAll
    static void clean() {
        engine.stop();
    }

    @Test
    public void test01() throws InterruptedException {
        final List<TestHoo> data = new ArrayList<>();

        TestServerStreamingRpcService clientService = client.createHertsRpcService(TestServerStreamingRpcService.class);
        clientService.test01("id", "no", new StreamObserver<TestHoo>() {
            @Override
            public void onNext(TestHoo value) {
                data.add(value);
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
            }
        });

        Thread.sleep(2000);
        assertEquals(10, data.size());
        assertEquals(0.1, data.get(0).getC01());
        assertEquals("val", data.get(0).getD01().get("key"));
        assertEquals("hi", data.get(0).getE01().get(0));
        assertEquals(0, data.get(0).getF01().size());
        assertNotNull(client.getChannel());
    }

    @Test
    public void test02() throws InterruptedException {
        final int[] data = new int[1];
        TestServerStreamingRpcService clientService = client.createHertsRpcService(TestServerStreamingRpcService.class);
        clientService.test02(1, new StreamObserver<TestHoo>() {
            @Override
            public void onNext(TestHoo value) {
                data[0] = value.getB01();
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
            }
        });

        Thread.sleep(2000);
        assertEquals(1, data[0]);
    }

    @Test
    public void error01() throws InterruptedException {
        TestServerStreamingRpcService clientService = client.createHertsRpcService(TestServerStreamingRpcService.class);
        final boolean[] isErr = {false};
        clientService.error01(1, new StreamObserver<byte[]>() {
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

        Thread.sleep(2000);
        assertTrue(isErr[0]);
    }
}
