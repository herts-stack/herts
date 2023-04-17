package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import java.util.Map;

public interface HttpService extends HertsCoreService {
    Map<String, String> httpTest01(String id, String value);
    boolean httpTest02();

    void httpTest03();
}
