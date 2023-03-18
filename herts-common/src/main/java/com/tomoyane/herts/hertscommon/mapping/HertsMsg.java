package com.tomoyane.herts.hertscommon.mapping;

import org.msgpack.annotation.Message;

import java.util.Map;

@Message
public class HertsMsg {
    private Map<String, Object> hertsBody;

    public HertsMsg() {
    }

    public HertsMsg(Map<String, Object> hertsBody) {
        this.hertsBody = hertsBody;
    }

    public Map<String, Object> getHertsBody() {
        return hertsBody;
    }

    public void setHertsBody(Map<String, Object> hertsBody) {
        this.hertsBody = hertsBody;
    }
}
