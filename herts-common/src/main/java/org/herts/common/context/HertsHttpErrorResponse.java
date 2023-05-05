package org.herts.common.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.herts.common.exception.http.HertsHttpError400;
import org.herts.common.exception.http.HertsHttpError401;
import org.herts.common.exception.http.HertsHttpError402;
import org.herts.common.exception.http.HertsHttpError403;
import org.herts.common.exception.http.HertsHttpError404;
import org.herts.common.exception.http.HertsHttpError405;
import org.herts.common.exception.http.HertsHttpError406;
import org.herts.common.exception.http.HertsHttpError407;
import org.herts.common.exception.http.HertsHttpError408;
import org.herts.common.exception.http.HertsHttpError409;
import org.herts.common.exception.http.HertsHttpError410;
import org.herts.common.exception.http.HertsHttpError411;
import org.herts.common.exception.http.HertsHttpError412;
import org.herts.common.exception.http.HertsHttpError413;
import org.herts.common.exception.http.HertsHttpError414;
import org.herts.common.exception.http.HertsHttpError415;
import org.herts.common.exception.http.HertsHttpError416;
import org.herts.common.exception.http.HertsHttpError417;
import org.herts.common.exception.http.HertsHttpError418;
import org.herts.common.exception.http.HertsHttpError421;
import org.herts.common.exception.http.HertsHttpError422;
import org.herts.common.exception.http.HertsHttpError423;
import org.herts.common.exception.http.HertsHttpError424;
import org.herts.common.exception.http.HertsHttpError425;
import org.herts.common.exception.http.HertsHttpError426;
import org.herts.common.exception.http.HertsHttpError427;
import org.herts.common.exception.http.HertsHttpError428;
import org.herts.common.exception.http.HertsHttpError429;
import org.herts.common.exception.http.HertsHttpError431;
import org.herts.common.exception.http.HertsHttpError451;
import org.herts.common.exception.http.HertsHttpError500;
import org.herts.common.exception.http.HertsHttpErrorException;

import java.io.Serializable;

/**
 * Herts http error response for internal message
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
        switch (this.statusCode) {
            case Status500:
                throw new HertsHttpError500(this.message);
            case Status400:
                throw new HertsHttpError400(this.message);
            case Status401:
                throw new HertsHttpError401(this.message);
            case Status402:
                throw new HertsHttpError402(this.message);
            case Status403:
                throw new HertsHttpError403(this.message);
            case Status404:
                throw new HertsHttpError404(this.message);
            case Status405:
                throw new HertsHttpError405(this.message);
            case Status406:
                throw new HertsHttpError406(this.message);
            case Status407:
                throw new HertsHttpError407(this.message);
            case Status408:
                throw new HertsHttpError408(this.message);
            case Status409:
                throw new HertsHttpError409(this.message);
            case Status410:
                throw new HertsHttpError410(this.message);
            case Status411:
                throw new HertsHttpError411(this.message);
            case Status412:
                throw new HertsHttpError412(this.message);
            case Status413:
                throw new HertsHttpError413(this.message);
            case Status414:
                throw new HertsHttpError414(this.message);
            case Status415:
                throw new HertsHttpError415(this.message);
            case Status416:
                throw new HertsHttpError416(this.message);
            case Status417:
                throw new HertsHttpError417(this.message);
            case Status418:
                throw new HertsHttpError418(this.message);
            case Status421:
                throw new HertsHttpError421(this.message);
            case Status422:
                throw new HertsHttpError422(this.message);
            case Status423:
                throw new HertsHttpError423(this.message);
            case Status424:
                throw new HertsHttpError424(this.message);
            case Status425:
                throw new HertsHttpError425(this.message);
            case Status426:
                throw new HertsHttpError426(this.message);
            case Status427:
                throw new HertsHttpError427(this.message);
            case Status428:
                throw new HertsHttpError428(this.message);
            case Status429:
                throw new HertsHttpError429(this.message);
            case Status431:
                throw new HertsHttpError431(this.message);
            case Status451:
                throw new HertsHttpError451(this.message);
            default:
                throw new HertsHttpError500(this.message);
        }
    }
}
