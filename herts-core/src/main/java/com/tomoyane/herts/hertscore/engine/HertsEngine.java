package com.tomoyane.herts.hertscore.engine;

import io.grpc.Server;

public interface HertsEngine {
    void start();
    Server getServer();
}
