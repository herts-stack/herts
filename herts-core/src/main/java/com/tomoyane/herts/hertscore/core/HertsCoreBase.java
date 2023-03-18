package com.tomoyane.herts.hertscore.core;

import com.tomoyane.herts.hertscommon.enums.HertsCoreType;

public class HertsCoreBase {
    private final HertsCoreType coreType;

    public HertsCoreBase(HertsCoreType rpcType) {
        this.coreType = rpcType;
    }

    public HertsCoreType getCoreType() {
        return coreType;
    }
}
