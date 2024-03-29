package org.hertsstack.e2etest.unary_rpc;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;
import org.hertsstack.e2etest.common.HelloRequest;

import java.util.List;
import java.util.Map;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryRpcService01 extends HertsService {
    String test01(String id, String value);
    boolean test02();
    Map<String, String> test03();
    boolean test100(HelloRequest req);
    List<String> test101(Map<String, String> a, List<String> b);
    void test102();
    void error01();
    void error02();
    void error03();
}
