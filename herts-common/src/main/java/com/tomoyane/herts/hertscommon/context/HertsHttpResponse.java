package com.tomoyane.herts.hertscommon.context;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HertsHttpResponse implements Serializable {
    @JsonProperty
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "";
    }
}
