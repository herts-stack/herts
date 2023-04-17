package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.service.HttpCoreServiceCore;

import java.util.Collections;
import java.util.Map;

public class HttpServiceImpl extends HttpCoreServiceCore implements HttpService {

    public HttpServiceImpl() {
    }

    @Override
    public Map<String, String> httpTest01(String id, String value) {
        System.out.println("------------ test01 RPC ----------- ");
        System.out.println("Id = " + id + " value = " + value);
        return Collections.singletonMap("key", "value!!!");
    }

    @Override
    public boolean httpTest02() {
        System.out.println("------------ test02 RPC ----------- ");
        return false;
    }

    @Override
    public void httpTest03() {
        System.out.println("------------ test03 RPC ----------- ");
    }
}
