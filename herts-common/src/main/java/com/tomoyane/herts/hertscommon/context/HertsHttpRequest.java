package com.tomoyane.herts.hertscommon.context;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class HertsHttpRequest implements Serializable {
    @JsonProperty
    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Object[] getDataValues() {
        return this.data.values().toArray();
    }

    @Override
    public String toString() {
        return "";
    }
}
