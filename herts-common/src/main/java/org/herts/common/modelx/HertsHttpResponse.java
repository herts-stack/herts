package org.herts.common.modelx;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.herts.common.modelx.HertsHttpMsg;

import java.io.Serializable;

/**
 * Herts http response for internal message
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpResponse implements Serializable {
    @JsonProperty
    private HertsHttpMsg payload;
    @JsonProperty
    private String exceptionCauseMessage;

    public HertsHttpMsg getPayload() {
        return payload;
    }

    public void setPayload(HertsHttpMsg payload) {
        this.payload = payload;
    }

    public String getExceptionCauseMessage() {
        return exceptionCauseMessage;
    }

    public void setExceptionCauseMessage(String exceptionCauseMessage) {
        this.exceptionCauseMessage = exceptionCauseMessage;
    }
}
