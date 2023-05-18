package org.herts.example.duplexstreaming_rpc;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsDuplexStreamingService;

import java.util.logging.Logger;

public class DuplexStreamingServiceImpl extends HertsDuplexStreamingService<DuplexStreamingService, DuplexStreamingReceiver> implements DuplexStreamingService {
    private static final Logger logger = HertsLogger.getLogger(DuplexStreamingServiceImpl.class.getSimpleName());

    public DuplexStreamingServiceImpl() {
        System.out.println("New");
    }

    @Override
    public void hello01() {
    }

    @Override
    public void hello02() {
    }
}
