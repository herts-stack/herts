package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsType;

/**
 * Herts bidirectional streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class BidirectionalStreamingCoreServiceCore extends HertsCoreCoreBase {

    public BidirectionalStreamingCoreServiceCore() {
        super(HertsType.BidirectionalStreaming);
    }
}
