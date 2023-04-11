package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.context.HertsMsg;

public class HelloResponse extends HertsMsg {
    private int code;
    private long timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
