package com.tomoyane.herts.hertscore.core;

import io.grpc.Server;

public interface HertsEngine {
    void start();
    Server getServer();
}
