package org.herts.core.modelx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.herts.core.exception.http.HttpErrorException;

import java.io.Serializable;

/**
 * Herts http error response for internal message
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class InternalHttpErrorResponse implements Serializable {
    @JsonProperty
    private HttpErrorException.StatusCode statusCode;
    @JsonProperty
    private String message;

    public HttpErrorException.StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpErrorException.StatusCode statusCode) {
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
