package org.herts.e2etest.unary_rpc;

import org.herts.core.logger.HertsLogger;
import org.herts.core.service.HertsServiceUnary;

import java.util.logging.Logger;

public class UnaryServiceImpl02 extends HertsServiceUnary<UnaryRpcService02> implements UnaryRpcService02 {
    private static final Logger logger = HertsLogger.getLogger(UnaryServiceImpl02.class.getSimpleName());

    public UnaryServiceImpl02() {
    }

    @Override
    public String hello01(String id, String value) {
        logger.info("------------ UnaryRpc hello01 RPC");
        return "Helllo!!!";
    }
}
