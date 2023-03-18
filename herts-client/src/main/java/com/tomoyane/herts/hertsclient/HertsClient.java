package com.tomoyane.herts.hertsclient;

import io.grpc.Channel;

public class HertsClient {
    private final Channel channel;
    public HertsClient(Channel channel) {
        this.channel = channel;
    }
}
