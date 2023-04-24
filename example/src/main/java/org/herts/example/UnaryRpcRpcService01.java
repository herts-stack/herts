package org.herts.example;

import org.herts.common.service.HertsRpcService;

import java.util.Map;

public interface UnaryRpcRpcService01 extends HertsRpcService {
    String test01(String id, String value);
    boolean test02();
    Map<String, String> test03();
    boolean test100(HelloRequest req);
}
