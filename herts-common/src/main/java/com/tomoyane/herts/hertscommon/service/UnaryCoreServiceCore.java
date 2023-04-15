package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsType;

/**
 * Herts unary streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class UnaryCoreServiceCore extends HertsCoreCoreBase {

    public UnaryCoreServiceCore() {
        super(HertsType.Unary);
    }
}
