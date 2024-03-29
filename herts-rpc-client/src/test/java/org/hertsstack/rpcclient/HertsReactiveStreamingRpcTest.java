package org.hertsstack.rpcclient;

import org.hertsstack.rpc.GrpcServerOption;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HertsReactiveStreamingRpcTest {
    private static final int port = 9999;

    private static HertsRpcServerEngine engine;
    private static HertsRpcClient client;

    @BeforeAll
    static void init() throws InterruptedException {
        try {
            Thread t = new Thread(() -> {
                TestReactiveStreamingRpcService service = new TestReactiveStreamingRpcServiceImpl();
                GrpcServerOption option = new GrpcServerOption();
                option.setPort(port);
                engine = HertsRpcServerEngineBuilder.builder(option)
                        .registerHertsReactiveRpcService(service)
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
                .registerHertsRpcServiceInterface(TestReactiveStreamingRpcService.class)
                .registerHertsRpcReceiver(new TestReactiveStreamingRpcReceiverImpl())
                .connect();
    }

    @AfterAll
    static void clean() {
        engine.stop();
        client.getChannel().shutdown();
    }

    @Test
    public void test01() {
        TestReactiveStreamingRpcService clientService = client.createHertsRpcService(TestReactiveStreamingRpcService.class);
        String id = clientService.test01();
        assertNotNull(id);
    }

    @Test
    public void test02() {
        TestReactiveStreamingRpcService clientService = client.createHertsRpcService(TestReactiveStreamingRpcService.class);
        boolean res = clientService.test02(999, "dataa");
        assertTrue(res);
    }

    @Test
    public void test03() {
        TestReactiveStreamingRpcService clientService = client.createHertsRpcService(TestReactiveStreamingRpcService.class);

        String key = "hoo_key";
        String val = "hoo_val";
        TestHoo hoo = new TestHoo();
        hoo.setD01(Collections.singletonMap(key, val));

        Map<String, String> res = clientService.test03(hoo);
        assertEquals(res.get(key), val);
    }

    @Test
    public void test04() {
        TestReactiveStreamingRpcService clientService = client.createHertsRpcService(TestReactiveStreamingRpcService.class);
        TestFoo res = clientService.test04(null, null);
        assertEquals(res.getA01(), "OK!");
    }
}
