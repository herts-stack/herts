package com.tomoyane.herts.hertscommon.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.msgpack.annotation.Message;
import org.msgpack.jackson.dataformat.MessagePackFactory;

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
