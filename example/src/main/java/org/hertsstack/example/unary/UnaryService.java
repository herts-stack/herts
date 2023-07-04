package org.hertsstack.example.unary;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryService extends HertsService {
    String helloWorld();
}
