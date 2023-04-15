package com.tomoyane.herts.hertscommon.service;

import com.tomoyane.herts.hertscommon.context.HertsType;

/**
 * Herts client streaming service
 * @author Herts Contributer
 * @version 1.0.0
 */
public abstract class ClientStreamingCoreServiceCore extends HertsCoreCoreBase {

    public ClientStreamingCoreServiceCore() {
        super(HertsType.ClientStreaming);
    }
}
