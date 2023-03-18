package com.tomoyane.herts.hertscore.core;

import com.tomoyane.herts.hertscommon.enums.RpcType;

public abstract class UnaryClientServiceCore extends HertsCoreBase {

    public UnaryClientServiceCore() {
        super(RpcType.Unary);
    }

    public void coreMethod01() {
    }
}
