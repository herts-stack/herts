package org.hertsstack.core.modelx;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Herts http response for internal message
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class InternalHttpResponse implements Serializable {
    @JsonProperty
    private InternalHttpMsg payload;

    public InternalHttpMsg getPayload() {
        return payload;
    }

    public void setPayload(InternalHttpMsg payload) {
        this.payload = payload;
    }
}
