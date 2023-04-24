package org.herts.example;

import org.herts.common.service.HertsRpcService;

import java.util.Map;

public interface HttpService extends HertsRpcService {
    Map<String, String> httpTest01(String id, String value);
    boolean httpTest02();

    void httpTest03();
}
