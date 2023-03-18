package com.tomoyane.herts.hertscore.core;

import com.tomoyane.herts.hertscommon.enums.RpcType;

public class HertsCoreBase {
    private final RpcType rpcType;

    public HertsCoreBase(RpcType rpcType) {
        this.rpcType = rpcType;
    }

    public RpcType getRpcType() {
        return rpcType;
    }

}
