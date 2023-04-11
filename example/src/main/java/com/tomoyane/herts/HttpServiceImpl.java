package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HttpCoreServiceCore;

public class HttpServiceImpl extends HttpCoreServiceCore implements HttpService {

    public HttpServiceImpl() {
    }

    @Override
    public String test01(String id, String value) {
        System.out.println("------------ test01 RPC ----------- ");
        System.out.println("Id = " + id + " value = " + value);
        return "Response NAME!!!!";
    }
}
