package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsRpcService;

import java.util.Map;

public interface HttpService extends HertsRpcService {
    Map<String, String> httpTest01(String id, String value);
    boolean httpTest02();

    void httpTest03();
}
