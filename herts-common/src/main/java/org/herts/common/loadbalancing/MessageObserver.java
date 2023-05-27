package org.herts.common.loadbalancing;

public interface MessageObserver {
    void receive(byte[] payload);
}
