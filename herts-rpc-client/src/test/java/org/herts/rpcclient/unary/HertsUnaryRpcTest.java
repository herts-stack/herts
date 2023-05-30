package org.herts.rpcclient.unary;

import org.herts.common.exception.rpc.HertsRpcErrorException;
import org.herts.common.service.HertsService;
import org.herts.rpc.engine.GrpcServerOption;
import org.herts.rpc.engine.HertsRpcServerEngineBuilder;
import org.herts.rpc.engine.HertsRpcServerEngine;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;
import org.herts.rpcclient.TestFoo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class HertsUnaryRpcTest {
    private static final int port = 9999;
    private static final HertsService service = new TestUnaryRpcServiceImpl();
    private static HertsRpcServerEngine engine;
    private static HertsRpcClient client;

    @BeforeAll
    static void init() throws InterruptedException {
        try {
            var t = new Thread(() -> {
                GrpcServerOption option = new GrpcServerOption();
                option.setPort(port);
                engine = HertsRpcServerEngineBuilder.builder(option)
                        .registerHertsRpcService(service)
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
                .registerHertsRpcServiceInterface(TestUnaryRpcService.class)
                .connect();
    }

    @AfterAll
    static void clean() {
        engine.stop();
    }

    @Test
    public void test01() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);
        var result = rpc.test01("test_id", "test_value");

        assertTrue(result.contains("test_id"));
        assertTrue(result.contains("test_value"));
    }

    @Test
    public void test02() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);
        var result = rpc.test02();

        assertTrue(result);
    }

    @Test
    public void test03() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);
        var result = rpc.test03(100, 0.99);
        var value = result.get("key");

        assertTrue(value.contains("100"));
        assertTrue(value.contains("0.99"));
    }

    @Test
    public void test04() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);
        var result = rpc.test04(Collections.singletonMap("key", "map_val"), Collections.singletonList("hello"));

        assertTrue(result.contains("map_val"));
        assertTrue(result.contains("hello"));
    }

    @Test
    public void test05() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);
        TestFoo foo = new TestFoo();
        foo.setA01("a");
        foo.setB01(100);
        foo.setC01(0.999);
        foo.setD01(Collections.singletonMap("key", "value"));
        foo.setE01(Collections.singletonList("list"));
        foo.setF01(Collections.emptySet());

        var result = rpc.test05(foo);
        assertNotNull(result);
    }

    @Test
    public void test06() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);
        rpc.test06();
    }

    @Test
    public void error01() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);

        try {
            rpc.error01();
            throw new RuntimeException("Invalid exception");
        } catch (HertsRpcErrorException ex) {
            assertEquals(HertsRpcErrorException.StatusCode.Status13, ex.getStatusCode());
            assertTrue(ex.getMessage().contains("Unexpected error occurred"));
        }
    }

    @Test
    public void error02() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);

        try {
            rpc.error02();
            throw new RuntimeException("Invalid exception");
        } catch (HertsRpcErrorException ex) {
            assertEquals("error02", ex.getMessage());
            assertEquals(HertsRpcErrorException.StatusCode.Status2, ex.getStatusCode());
        }
    }

    @Test
    public void error03() {
        TestUnaryRpcService rpc = client.createHertsRpcService(TestUnaryRpcService.class);

        try {
            rpc.error03();
            throw new RuntimeException("Invalid exception");
        } catch (HertsRpcErrorException ex) {
            assertEquals("error03", ex.getMessage());
            assertEquals(HertsRpcErrorException.StatusCode.Status10, ex.getStatusCode());
        }
    }
}
