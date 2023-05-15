package org.herts.httpclient;

import org.herts.common.exception.http.HertsHttpErrorException;

public class InternalServerError extends HertsHttpErrorException {
    public InternalServerError(String msg){
        super(StatusCode.Status500, msg);
    }
}
