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
 */
public class InternalHttpErrorResponse implements Serializable {
    @JsonProperty
    private StatusCode statusCodeEnum;
    @JsonProperty
    private String statusCode;
    @JsonProperty
    private String message;

    public StatusCode getStatusCodeEnum() {
        return statusCodeEnum;
    }

    public void setStatusCodeEnum(StatusCode statusCodeEnum) {
        this.statusCodeEnum = statusCodeEnum;
        this.statusCode = statusCodeEnum.getStringCode();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    @JsonIgnore
    public void throwHertsHttpErrorException() throws HttpErrorException {
        String statusCode = this.statusCodeEnum.getStringCode();
        String customMsg = "Http status is " + statusCode + ". Error message = " + this.message;
        throw new HttpErrorException(this.statusCodeEnum, customMsg);
    }
}
