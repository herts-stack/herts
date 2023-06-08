package org.herts.example.reactivesteaming;

import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsReactiveService;

@HertsRpcService(value = HertsType.Reactive)
public interface ReactiveService extends HertsReactiveService {
    String helloWorld(String value);
}
