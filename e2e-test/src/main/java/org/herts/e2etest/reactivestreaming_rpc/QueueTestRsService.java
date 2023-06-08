package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsReactiveService;

@HertsRpcService(value = HertsType.Reactive)
public interface QueueTestRsService extends HertsReactiveService {
    void callFoo(String uniqId);
}
