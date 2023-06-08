package org.herts.example.jwtrpc;

import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryService extends HertsService {
    String helloWorld();
}
