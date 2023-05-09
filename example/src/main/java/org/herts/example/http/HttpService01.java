package org.herts.example;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;
import org.herts.example.model.TestData;

import java.util.List;
import java.util.Map;

@HertsHttp
public interface HttpService01 extends HertsService {
    Map<String, String> httpTest01(String id, String value);
    boolean httpTest02();

    void httpTest03();

    TestData httpTest04(TestData testData);

    String httpTest05(List<String> a, Map<String, String> b);

    String httpTest06(String a, boolean b, int c, Integer d, double e);

    String httpTest07();
}
