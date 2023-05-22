package org.herts.example.duplexstreaming_rpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsDuplexService;

@HertsRpcService(value = HertsType.DuplexStreaming)
public interface DuplexStreamingService extends HertsDuplexService {
    void hello01();
    void hello02();
}
