package org.herts.httpclient;

import org.herts.core.exception.http.HttpErrorException;

public class InternalServerError extends HttpErrorException {
    public InternalServerError(String msg){
        super(StatusCode.Status500, msg);
    }
}
