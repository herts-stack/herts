package org.hertsstack.httpclient;

import org.hertsstack.core.exception.http.HttpErrorException;

public class InternalServerError extends HttpErrorException {
    public InternalServerError(String msg){
        super(StatusCode.Status500, msg);
    }
}
