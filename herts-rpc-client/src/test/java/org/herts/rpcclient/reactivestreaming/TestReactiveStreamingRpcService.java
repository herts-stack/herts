package org.herts.rpcclient.reactivestreaming;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsReactiveService;
import org.herts.rpcclient.TestFoo;
import org.herts.rpcclient.TestHoo;

import java.util.Map;

@HertsRpcService(value = HertsType.Reactive)
public interface TestReactiveStreamingRpcService extends HertsReactiveService {
    String test01();
    boolean test02(int id, String data);
    Map<String, String> test03(TestHoo hoo);
    TestFoo test04(String id, Map<String, String> data01);
}
