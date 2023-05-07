package org.herts.example;

import org.herts.common.annotation.HertsRpc;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

@HertsRpc(value = HertsType.Unary)
public interface UnaryRpcService02 extends HertsService {
    String hello01(String id, String value);
}
