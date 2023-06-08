package org.herts.httpclient;

import org.herts.core.service.HertsServiceHttp;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestHertsServiceImpl extends HertsServiceHttp<TestHertsService> implements TestHertsService {
    @Override
    public String test01() {
        return "test01";
    }

    @Override
    public String test02(String a, boolean b, int c, Integer d, double e) {
        return String.format("a=%s, b=%s, c=%d, d=%d, e=%f  ", a, b, c, d, e);
    }

    @Override
    public int test03() {
        return 9999;
    }

    @Override
    public boolean test04() {
        return true;
    }

    @Override
    public long test05() {
        return 922337203685477580L;
    }

    @Override
    public double test06() {
        return 1.999999;
    }

    @Override
    public List<String> test07() {
        return Collections.singletonList("TEST_LIST");
    }

    @Override
    public Map<String, String> test08() {
        return Collections.singletonMap("key", "value");
    }

    @Override
    public String test10(List<String> a, Map<String, String> b) {
        return String.format("a=%s, b=%s", a.get(0), b.get("test10"));
    }

    @Override
    public TestDataModel test11(TestDataModel a) {
        return a;
    }

    @Override
    public void test12() {
        throw new BadRequest("test12");
    }

    @Override
    public void test13() {
        throw new InternalServerError("test13");
    }
}
