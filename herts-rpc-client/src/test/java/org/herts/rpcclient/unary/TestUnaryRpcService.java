package org.herts.rpcclient.unary;

import org.herts.common.annotation.HertsRpc;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;
import org.herts.rpcclient.TestFoo;

import java.util.List;
import java.util.Map;

@HertsRpc(value = HertsType.Unary)
public interface TestUnaryRpcService extends HertsService {
    String test01(String id, String value);
    boolean test02();
    Map<String, String> test03(int a, double b);
    List<String> test04(Map<String, String> a, List<String> b);
    TestFoo test05(TestFoo foo);
    void test06();
    void error01();
    void error02();
    void error03();
}
