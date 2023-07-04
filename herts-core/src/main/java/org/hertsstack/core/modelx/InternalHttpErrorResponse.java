package org.hertsstack.core.modelx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hertsstack.core.exception.http.HttpErrorException;

import java.io.Serializable;
import org.hertsstack.core.exception.http.HttpErrorException.StatusCode;

/**
 * Herts http error response for internal message
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class InternalHttpErrorResponse implements Serializable {
    @JsonProperty
    private StatusCode statusCode;
    @JsonProperty
    private String message;

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public void throwHertsHttpErrorException() throws HttpErrorException {
        throw new HttpErrorException(this.statusCode, this.message);
    }
}
