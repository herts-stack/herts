package org.herts.e2etest.unary_rpc;

import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryRpcService02 extends HertsService {
    String hello01(String id, String value);
}
