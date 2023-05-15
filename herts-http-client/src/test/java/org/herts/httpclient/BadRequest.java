package org.herts.httpclient;

import org.herts.common.exception.http.HertsHttpErrorException;

public class BadRequest extends HertsHttpErrorException {
    public BadRequest(String msg){
        super(StatusCode.Status400, msg);
    }
}
