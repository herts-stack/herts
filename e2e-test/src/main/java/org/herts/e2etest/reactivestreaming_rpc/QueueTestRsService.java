package org.herts.e2etest.reactivestreaming_rpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.reactive.HertsReactiveService;

@HertsRpcService(value = HertsType.Reactive)
public interface QueueTestRsService extends HertsReactiveService {
    void callFoo(String uniqId);
}
