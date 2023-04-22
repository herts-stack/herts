package com.tomoyane.herts.hertscommon.context;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Map;

/**
 * Herts http request for internal message
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpRequest implements Serializable {
    private Map<String, Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @JsonIgnore
    public Object[] getDataValues() {
        return this.data.values().toArray();
    }

    @Override
    public String toString() {
        return "";
    }
}
