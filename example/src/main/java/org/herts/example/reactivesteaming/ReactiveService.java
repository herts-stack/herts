package org.herts.example.reactivesteaming;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.reactive.HertsReactiveService;

@HertsRpcService(value = HertsType.Reactive)
public interface ReactiveService extends HertsReactiveService {
    String helloWorld(String value);
}
