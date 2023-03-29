package com.tomoyane.herts;

import com.tomoyane.herts.hertscore.UnaryServiceCore;

public class UnaryRpcServiceImpl02 extends UnaryServiceCore implements UnaryRpcService02 {

    public UnaryRpcServiceImpl02() {
    }

    @Override
    public String hello01(String id, String value) {
        System.out.println("------------ hello01 RPC ----------- ");
        return "Helllo!!!";
    }
}
