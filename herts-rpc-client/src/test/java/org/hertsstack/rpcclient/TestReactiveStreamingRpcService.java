package org.hertsstack.rpcclient;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsReactiveService;

import java.util.Map;

@HertsRpcService(value = HertsType.Reactive)
public interface TestReactiveStreamingRpcService extends HertsReactiveService {
    String test01();
    boolean test02(int id, String data);
    Map<String, String> test03(TestHoo hoo);
    TestFoo test04(String id, Map<String, String> data01);
}
