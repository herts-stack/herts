package org.herts.example.tlsrpc;

import org.herts.common.service.HertsUnaryService;

public class RpcServiceImpl extends HertsUnaryService<RpcService> implements RpcService {
    @Override
    public void hello() {
    }
}
