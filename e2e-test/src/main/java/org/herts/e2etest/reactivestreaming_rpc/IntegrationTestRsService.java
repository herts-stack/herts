package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsReactiveService;
import org.herts.e2etest.common.HelloRequest;
import org.herts.e2etest.common.HelloResponse01;

import java.util.Map;

@HertsRpcService(value = HertsType.Reactive)
public interface IntegrationTestRsService extends HertsReactiveService {
    void hello01();
    void hello02(String id);
    Map<String, String> hello03(HelloRequest req);
    HelloResponse01 hello04(String id, Map<String, String> data01);
}
