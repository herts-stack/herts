package org.hertsstack.httpclient;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.service.HertsService;

import java.util.List;
import java.util.Map;

@HertsHttp
public interface TestHertsService extends HertsService {
    String test01();
    String test02(String a, boolean b, int c, Integer d, double e);
    int test03();
    boolean test04();
    long test05();
    double test06();
    List<String> test07();
    Map<String, String> test08();
    String test10(List<String> a, Map<String, String> b);
    TestDataModel test11(TestDataModel a);
    void test12();
    void test13();
}
