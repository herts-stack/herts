package org.hertsstack.gateway;

import org.hertsstack.core.exception.http.HttpErrorException;
import org.hertsstack.httpclient.HertsHttpClient;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HertsGatewayTest {
    private static HertsGatewayEngine gatewayEngine;
    private static HertsRpcServerEngine rpcEngine;
    private static TestUnaryRpcService service;

    @BeforeAll
    static void init() throws InterruptedException {
        try {
            Thread t = new Thread(() -> {
                rpcEngine = HertsRpcServerEngineBuilder.builder()
                        .registerHertsRpcService(new TestUnaryServiceImpl())
                        .build();
                rpcEngine.start();
            });
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Thread.sleep(2000);

        try {
            Thread t = new Thread(() -> {
                gatewayEngine = HertsGatewayBuilder.builder()
                        .gatewayPort(9876)
                        .rpcHost("localhost")
                        .rpcPort(9000)
                        .registerHertsRpcService(TestUnaryRpcService.class, null)
                        .build();
                gatewayEngine.start();
            });
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Thread.sleep(2000);

        HertsHttpClient client = HertsHttpClient
                .builder("localhost")
                .registerHertsService(TestUnaryRpcService.class)
                .secure(false)
                .gatewayApi(true)
                .port(9876)
                .build();

        service = client.createHertsService(TestUnaryRpcService.class);
    }

    @AfterAll
    static void clean() {
        try {
            gatewayEngine.stop();
            rpcEngine.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test01() {
        String res = service.test01("id", "value");
        assertEquals("Response!", res);
    }

    @Test
    public void test02() {
        boolean res = service.test02();
        assertFalse(res);
    }

    @Test
    public void test03() {
        Map<String, String> res = service.test03();
        assertEquals("Value", res.get("Key"));
    }

    @Test
    public void error401() {
        try {
            service.error401();
        } catch (HttpErrorException ex) {
            assertTrue(ex.getMessage().contains("Unit test error"));
            assertEquals(HttpErrorException.StatusCode.Status401, ex.getStatusCode());
        }
    }

    @Test
    public void error500() {
        try {
            service.error500();
        } catch (HttpErrorException ex) {
            assertEquals(HttpErrorException.StatusCode.Status500, ex.getStatusCode());
        }
    }
}
