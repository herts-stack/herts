package com.tomoyane.herts;

import com.tomoyane.herts.hertscore.UnaryCoreServiceCore;

public class UnaryRpcCoreServiceImpl02 extends UnaryCoreServiceCore implements UnaryRpcCoreService02 {

    public UnaryRpcCoreServiceImpl02() {
    }

    @Override
    public String hello01(String id, String value) {
        System.out.println("------------ hello01 RPC ----------- ");
        return "Helllo!!!";
    }
}
