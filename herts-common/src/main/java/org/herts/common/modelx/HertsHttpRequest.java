package org.herts.common.modelx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.herts.common.modelx.HertsHttpMsg;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Herts http request for internal message
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpRequest implements Serializable {
    @JsonProperty
    private List<HertsHttpMsg> payloads;

    public HertsHttpRequest() {
    }

    public List<HertsHttpMsg> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<HertsHttpMsg> payloads) {
        this.payloads = payloads;
    }

    @JsonIgnore
    public List<String> getKeyNames() {
        return this.payloads.stream().map(HertsHttpMsg::getKeyName).collect(Collectors.toList());
    }
}
