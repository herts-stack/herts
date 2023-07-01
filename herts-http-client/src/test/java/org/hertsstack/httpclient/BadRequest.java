package org.hertsstack.httpclient;

import org.hertsstack.core.exception.http.HttpErrorException;

public class BadRequest extends HttpErrorException {
    public BadRequest(String msg){
        super(StatusCode.Status400, msg);
    }
}
