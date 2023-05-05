package org.herts.common.context;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Herts http response for internal message
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpResponse implements Serializable {
    @JsonProperty
    private Payload payload;
    @JsonProperty
    private String exceptionCauseMessage;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getExceptionCauseMessage() {
        return exceptionCauseMessage;
    }

    public void setExceptionCauseMessage(String exceptionCauseMessage) {
        this.exceptionCauseMessage = exceptionCauseMessage;
    }
}
