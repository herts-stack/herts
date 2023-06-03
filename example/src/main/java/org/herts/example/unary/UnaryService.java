package org.herts.example.unary;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryService extends HertsService {
    String helloWorld();
}
