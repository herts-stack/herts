package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.common.service.UnaryRpcServiceRpc;

import java.util.logging.Logger;

public class UnaryRpcRpcServiceImpl02 extends UnaryRpcServiceRpc implements UnaryRpcRpcService02 {
    private static final Logger logger = HertsLogger.getLogger(UnaryRpcRpcServiceImpl02.class.getSimpleName());

    public UnaryRpcRpcServiceImpl02() {
    }

    @Override
    public String hello01(String id, String value) {
        logger.info("------------ UnaryRpc hello01 RPC");
        return "Helllo!!!";
    }
}
