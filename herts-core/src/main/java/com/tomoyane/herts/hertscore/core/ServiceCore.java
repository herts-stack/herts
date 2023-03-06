package com.tomoyane.herts.hertscore.core;

public interface ServiceCore {
    void setCoreSetting();
    void startServer();
    void stopServer();
    void blockUntilShutdown();
}
