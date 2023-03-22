package com.tomoyane.herts.hertscore.core;

import com.tomoyane.herts.hertscommon.enums.HertsCoreType;

public abstract class StreamingServiceCore extends HertsCoreBase {

    public StreamingServiceCore() {
        super(HertsCoreType.BidirectionalStreaming);
    }
}
