package org.herts.common.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Herts http request for internal message
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpRequest implements Serializable {
    @JsonProperty
    private List<Payload> payloads;

    public HertsHttpRequest() {
    }
    public List<Payload> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<Payload> payloads) {
        this.payloads = payloads;
    }

    @JsonIgnore
    public List<String> getKeyNames() {
        return this.payloads.stream().map(Payload::getKeyName).collect(Collectors.toList());
    }
}
