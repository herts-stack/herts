package org.hertsstack.core.exception.http;

import io.grpc.Status;

/**
 * Herts http error
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HttpErrorException extends RuntimeException {
    private final StatusCode statusCode;

    /**
     * Herts supports http status code
     */
    public enum StatusCode {
        Status300("300"),
        Status301("301"),
        Status302("302"),
        Status303("303"),
        Status304("304"),
        Status305("305"),
        Status306("306"),
        Status307("307"),
        Status308("308"),
        Status400("400"),
        Status401("401"),
        Status402("402"),
        Status403("403"),
        Status404("404"),
        Status405("405"),
        Status406("406"),
        Status407("407"),
        Status408("408"),
        Status409("409"),
        Status410("410"),
        Status411("411"),
        Status412("412"),
        Status413("413"),
        Status414("414"),
        Status415("415"),
        Status416("416"),
        Status417("417"),
        Status418("418"),
        Status421("421"),
        Status422("422"),
        Status423("423"),
        Status424("424"),
        Status425("425"),
        Status426("426"),
        Status427("427"),
        Status428("428"),
        Status429("429"),
        Status431("431"),
        Status451("451"),
        Status500("500"),
        Status501("501"),
        Status502("502"),
        Status503("503"),
        Status504("504"),
        Status505("505"),
        Status506("506"),
        Status507("507"),
        Status508("508"),
        Status510("510"),
        Status511("511");

        private final String value;

        StatusCode(final String value) {
            this.value = value;
        }

        public String getStringCode() {
            return this.value;
        }

        public int getIntegerCode() {
            return Integer.parseInt(this.value);
        }
    }

    public HttpErrorException(StatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public HttpErrorException(StatusCode statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }

    public HttpErrorException(StatusCode statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public HttpErrorException(StatusCode statusCode, String msg, Throwable cause) {
        super(msg, cause);
        this.statusCode = statusCode;
    }

    public static HttpErrorException from(Status status, String msg) {
        if (status.getCode() == Status.CANCELLED.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.UNKNOWN.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.INVALID_ARGUMENT.getCode()) {
            return new HttpErrorException(StatusCode.Status400, msg);
        } else if (status.getCode() == Status.DEADLINE_EXCEEDED.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.NOT_FOUND.getCode()) {
            return new HttpErrorException(StatusCode.Status404, msg);
        } else if (status.getCode() == Status.ALREADY_EXISTS.getCode()) {
            return new HttpErrorException(StatusCode.Status409, msg);
        } else if (status.getCode() == Status.PERMISSION_DENIED.getCode()) {
            return new HttpErrorException(StatusCode.Status403, msg);
        } else if (status.getCode() == Status.RESOURCE_EXHAUSTED.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.FAILED_PRECONDITION.getCode()) {
            return new HttpErrorException(StatusCode.Status422, msg);
        } else if (status.getCode() == Status.ABORTED.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.OUT_OF_RANGE.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.UNIMPLEMENTED.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.INTERNAL.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.UNAVAILABLE.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.DATA_LOSS.getCode()) {
            return new HttpErrorException(StatusCode.Status500, msg);
        } else if (status.getCode() == Status.UNAUTHENTICATED.getCode()) {
            return new HttpErrorException(StatusCode.Status401, msg);
        }
        return new HttpErrorException(StatusCode.Status500, "Unexpected error");
    }
    
    public StatusCode getStatusCode() {
        return statusCode;
    }
}
