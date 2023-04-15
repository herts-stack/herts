package com.tomoyane.herts.hertscommon.context;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Herts http response for internal message
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpResponse implements Serializable {
    @JsonProperty
    private Object data;

    public HertsHttpResponse(Object data) {
        this.data = data;
    }

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
