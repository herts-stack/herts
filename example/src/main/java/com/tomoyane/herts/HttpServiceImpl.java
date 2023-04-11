package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.UnaryCoreServiceCore;

public class HttpServiceImpl extends UnaryCoreServiceCore implements HttpService {

    public HttpServiceImpl() {
    }

    @Override
    public String test01(String id, String value) {
        System.out.println("------------ test01 RPC ----------- ");
        System.out.println("Id = " + id + " value = " + value);
        return "Response NAME!!!!";
    }
}
