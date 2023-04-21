package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.service.UnaryCoreServiceCore;

import java.util.logging.Logger;

public class UnaryRpcCoreServiceImpl02 extends UnaryCoreServiceCore implements UnaryRpcCoreService02 {
    private static final Logger logger = HertsLogger.getLogger(UnaryRpcCoreServiceImpl02.class.getSimpleName());

    public UnaryRpcCoreServiceImpl02() {
    }

    @Override
    public String hello01(String id, String value) {
        logger.info("------------ UnaryRpc hello01 RPC");
        return "Helllo!!!";
    }
}
