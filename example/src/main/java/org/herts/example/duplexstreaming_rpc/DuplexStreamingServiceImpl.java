package org.herts.example.duplexstreaming_rpc;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsDuplexStreamingService;

import java.util.Collections;
import java.util.logging.Logger;

public class DuplexStreamingServiceImpl extends HertsDuplexStreamingService<DuplexStreamingService, DuplexStreamingReceiver> implements DuplexStreamingService {
    private static final Logger logger = HertsLogger.getLogger(DuplexStreamingServiceImpl.class.getSimpleName());

    public DuplexStreamingServiceImpl() {
    }

    @Override
    public void hello01() {
        logger.info("------------ DuplexStreamingService hello01 RPC");
        var clientId = getClientId();
        broadcast(clientId).onReceivedHello01();
        broadcast(clientId).onReceivedHello02("TEST!", 9999);
        broadcast(clientId).onReceivedHello03(Collections.singletonMap("key", "value"));
    }

    @Override
    public void hello02() {
    }
}
