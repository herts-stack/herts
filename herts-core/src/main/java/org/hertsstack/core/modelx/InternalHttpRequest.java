package org.hertsstack.core.modelx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Herts http request for internal message
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class InternalHttpRequest implements Serializable {
    @JsonProperty
    private List<InternalHttpMsg> payloads;

    public InternalHttpRequest() {
    }

    public List<InternalHttpMsg> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<InternalHttpMsg> payloads) {
        this.payloads = payloads;
    }

    @JsonIgnore
    public List<String> getKeyNames() {
        return this.payloads.stream().map(InternalHttpMsg::getKeyName).collect(Collectors.toList());
    }
}
