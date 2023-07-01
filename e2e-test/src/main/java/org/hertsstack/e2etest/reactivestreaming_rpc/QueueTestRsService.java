package org.hertsstack.e2etest.reactivestreaming_rpc;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsReactiveService;

@HertsRpcService(value = HertsType.Reactive)
public interface QueueTestRsService extends HertsReactiveService {
    void callFoo(String uniqId);
}
