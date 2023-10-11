package org.hertsstack.example.reactivesteaming;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsReactiveService;
import org.hertsstack.example.commonmodel.Hoo;

@HertsRpcService(value = HertsType.Reactive)
public interface ReactiveService extends HertsReactiveService {
    String helloWorld(String value);
    Hoo getHoo();
}
