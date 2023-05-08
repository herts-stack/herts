package org.herts.rpcclient.unary;

import org.herts.common.service.HertsUnaryService;
import org.herts.rpcclient.TestFoo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestUnaryRpcServiceImpl extends HertsUnaryService<TestUnaryRpcService> implements TestUnaryRpcService {

    public TestUnaryRpcServiceImpl() {
    }

    @Override
    public String test01(String id, String value) {
        return "test01: " + id + " " + value;
    }

    @Override
    public boolean test02() {
        return true;
    }

    @Override
    public Map<String, String> test03(int a, double b) {
        var result = String.format("a=%d, b=%f", a, b);
        return Collections.singletonMap("key", result);
    }

    @Override
    public List<String> test04(Map<String, String> a, List<String> b) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, String> entries : a.entrySet()){
            result.add(entries.getValue());
        }
        result.addAll(b);
        return result;
    }

    @Override
    public TestFoo test05(TestFoo foo) {
        return foo;
    }

    @Override
    public void test06() {
    }
}
