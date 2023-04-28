package org.herts.example;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

import java.util.Map;

@HertsHttp
public interface HttpService extends HertsService {
    Map<String, String> httpTest01(String id, String value);
    boolean httpTest02();

    void httpTest03();
}
