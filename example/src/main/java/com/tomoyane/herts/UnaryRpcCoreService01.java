package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import java.util.Map;

public interface UnaryRpcCoreService01 extends HertsCoreService {
    String test01(String id, String value);
    boolean test02();
    Map<String, String> test03();
    boolean test100(HelloRequest req);
}
