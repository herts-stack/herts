package org.hertsstack.e2etest.unary_rpc;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryRpcService02 extends HertsService {
    String hello01(String id, String value);
}
