package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.HertsUnaryService;

import java.util.logging.Logger;

public class UnaryServiceServiceImpl02 extends HertsUnaryService<UnaryRpcRpcService02> implements UnaryRpcRpcService02 {
    private static final Logger logger = HertsLogger.getLogger(UnaryServiceServiceImpl02.class.getSimpleName());

    public UnaryServiceServiceImpl02() {
    }

    @Override
    public String hello01(String id, String value) {
        logger.info("------------ UnaryRpc hello01 RPC");
        return "Helllo!!!";
    }
}
