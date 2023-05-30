package org.herts.e2etest.unary_rpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface UnaryRpcService02 extends HertsService {
    String hello01(String id, String value);
}
