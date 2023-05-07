package org.herts.httpclient;

import org.herts.common.exception.http.HertsHttpError500;

public class InternalServerError extends HertsHttpError500 {
    public InternalServerError(String msg){
        super(msg);
    }
}
