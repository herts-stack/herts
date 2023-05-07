package org.herts.httpclient;

import org.herts.common.exception.http.HertsHttpError400;

public class BadRequest extends HertsHttpError400 {
    public BadRequest(String msg){
        super(msg);
    }
}
