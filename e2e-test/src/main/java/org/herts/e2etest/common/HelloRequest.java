package org.herts.e2etest.common;

import org.herts.core.modelx.HertsRpcMsg;

public class HelloRequest extends HertsRpcMsg {
    private String key;
    private boolean isOk;
    private int number;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
