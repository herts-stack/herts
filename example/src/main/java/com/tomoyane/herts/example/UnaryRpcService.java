package com.tomoyane.herts.example;

import com.tomoyane.herts.hertscore.service.HertsService;

import java.util.Map;

public interface UnaryRpcService extends HertsService {
    String test01(String id, String value);
    boolean test02();
    Map<String, String> test03();
    boolean test100(HelloRequest req);
}
