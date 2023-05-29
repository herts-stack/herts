package org.herts.e2etest.common;

import org.herts.common.modelx.HertsRpcMsg;

public class HelloResponse01 extends HertsRpcMsg {
    private int code;
    private long timestamp;
    private String test;

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

    public void setTest(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }
}
