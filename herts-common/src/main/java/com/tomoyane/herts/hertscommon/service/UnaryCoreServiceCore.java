package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsCoreCoreBase;

public abstract class UnaryCoreServiceCore extends HertsCoreCoreBase {

    public UnaryCoreServiceCore() {
        super(HertsCoreType.Unary);
    }
}
