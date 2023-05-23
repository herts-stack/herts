package org.herts.example.reactivestreaming_rpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsReactiveService;

@HertsRpcService(value = HertsType.Reactive)
public interface ReactiveStreamingService extends HertsReactiveService {
    void hello01();
    void hello02();
}
