package com.tomoyane.herts.hertscore.engine;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import io.grpc.Server;

public interface HertsCoreEngine {
    void start();
    Server getServer();

    HertsCoreType getHertCoreType();
}
