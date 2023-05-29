package org.herts.common.modelx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.herts.common.exception.http.HertsHttpErrorException;

import java.io.Serializable;

/**
 * Herts http error response for internal message
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpErrorResponse implements Serializable {
    @JsonProperty
    private HertsHttpErrorException.StatusCode statusCode;
    @JsonProperty
    private String message;

    public HertsHttpErrorException.StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HertsHttpErrorException.StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public void throwHertsHttpErrorException() throws HertsHttpErrorException {
        throw new HertsHttpErrorException(this.statusCode, this.message);
    }
}
