package org.hertsstack.gateway;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

import java.util.Map;

@HertsRpcService(value = HertsType.Unary)
public interface TestUnaryRpcService extends HertsService {
    String test01(String id, String value);
    boolean test02();
    Map<String, String> test03();
    void error401();
    void error500();
}
