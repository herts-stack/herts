package org.herts.example.unary_rpc;

import org.herts.common.annotation.HertsRpc;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import org.herts.example.common.HelloRequest;

import java.util.List;
import java.util.Map;

@HertsRpc(value = HertsType.Unary)
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
