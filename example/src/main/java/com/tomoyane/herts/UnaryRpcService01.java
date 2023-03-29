package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsService;

import java.util.Map;

public interface UnaryRpcService01 extends HertsService {
    String test01(String id, String value);
    boolean test02();
    Map<String, String> test03();
    boolean test100(HelloRequest req);
}
