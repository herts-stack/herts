package org.herts.rpcclient;

import org.herts.core.exception.rpc.HertsRpcErrorException;
import org.herts.core.service.HertsServiceUnary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestUnaryRpcServiceImpl extends HertsServiceUnary<TestUnaryRpcService> implements TestUnaryRpcService {

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
        String result = String.format("a=%d, b=%f", a, b);
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

    @Override
    public void error01() {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void error02() {
        throw new HertsRpcErrorException(HertsRpcErrorException.StatusCode.Status2, "error02");
    }

    @Override
    public void error03() {
        throw new HertsRpcErrorException(HertsRpcErrorException.StatusCode.Status10, "error03");
    }
}
