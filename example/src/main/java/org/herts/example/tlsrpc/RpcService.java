package org.herts.example.tlsrpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface RpcService extends HertsService {
    void hello();
}
