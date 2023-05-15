package org.herts.httpclient;

import org.herts.common.exception.http.HertsHttpErrorException;
import org.herts.http.engine.HertsHttpEngine;
import org.herts.http.engine.HertsHttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class HertsHttpClientTest {
    private static HertsHttpEngine engine;
    private static TestHertsService testHertsService;

    public HertsHttpClientTest() {
    }

    @BeforeAll
    static void init() throws InterruptedException {
        try {
            var t = new Thread(() -> {
                engine = HertsHttpServer.builder()
                        .setPort(8080)
                        .registerHertsHttpService(new TestHertsServiceImpl())
                        .build();
                engine.start();
            });
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Thread.sleep(1000);
        HertsHttpClientBase clientBase = HertsHttpClient
                .builder("localhost")
                .registerHertService(TestHertsService.class)
                .secure(false)
                .port(8080)
                .build();

        testHertsService = clientBase.createHertsService(TestHertsService.class);
    }

    @AfterAll
    static void clean() {
        try {
            engine.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test01() {
        var res = testHertsService.test01();
        assertEquals("test01", res);
    }

    @Test
    public void test02() {
        var doubleVal = 1.4;
        var res = testHertsService.test02("hello", true, 100, 200, doubleVal);
        assertTrue(res.contains("a=hello"));
        assertTrue(res.contains("b=true"));
        assertTrue(res.contains("c=100"));
        assertTrue(res.contains("d=200"));
        assertTrue(res.contains("e=1"));
    }

    @Test
    public void test03() {
        var res = testHertsService.test03();
        assertEquals(9999, res);
    }

    @Test
    public void test04() {
        var res = testHertsService.test04();
        assertTrue(res);
    }

    @Test
    public void test05() {
        var res = testHertsService.test05();
        assertEquals(922337203685477580L, res);
    }

    @Test
    public void test06() {
        var res = testHertsService.test06();
        assertTrue(String.format("%f", res).contains("1.999999"));
    }

    @Test
    public void test07() {
        var res = testHertsService.test07();
        assertEquals(1, res.size());
        assertEquals("TEST_LIST", res.get(0));
    }

    @Test
    public void test08() {
        var res = testHertsService.test08();
        assertEquals(1, res.size());
        assertEquals("value", res.get("key"));
    }

    @Test
    public void test10() {
        var res = testHertsService.test10(Collections.singletonList("hello"), Collections.singletonMap("test10", "value"));
        assertTrue(res.contains("hello"));
        assertTrue(res.contains("value"));
    }

    @Test
    public void test11() {
        var model = new TestDataModel();
        model.setA(Collections.singletonList("test11_list"));
        model.setB(Collections.singletonMap("test11_key", "value"));
        model.setD("test11_str");
        model.setE(1);
        model.setF(0.1);

        var res = testHertsService.test11(model);
        assertEquals(res.getA(), model.getA());
    }

    @Test
    public void test12() {
        try {
            testHertsService.test12();
            throw new RuntimeException("Invalid test");
        } catch (HertsHttpErrorException ex) {
            assertEquals(ex.getStatusCode(), HertsHttpErrorException.StatusCode.Status400);
        }
    }

    @Test
    public void test13() {
        try {
            testHertsService.test13();
            throw new RuntimeException("Invalid test");
        } catch (HertsHttpErrorException ex) {
            assertEquals(ex.getStatusCode(), HertsHttpErrorException.StatusCode.Status500);
        }
    }
}
