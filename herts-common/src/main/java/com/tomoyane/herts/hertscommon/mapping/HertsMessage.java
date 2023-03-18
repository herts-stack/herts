package com.tomoyane.herts.hertscommon.mapping;

import org.msgpack.annotation.Message;

@Message
public class HertsMessage {
    public String name;
    public double version;
}
