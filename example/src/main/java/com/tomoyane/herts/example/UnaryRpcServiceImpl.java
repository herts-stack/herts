package com.tomoyane.herts.example;

import com.tomoyane.herts.hertscore.core.UnaryServiceCore;

import java.util.Collections;
import java.util.Map;

public class UnaryRpcServiceImpl extends UnaryServiceCore implements UnaryRpcService {

    public UnaryRpcServiceImpl() {
    }

    public String test01(String id, String value) {
        System.out.println("------------ test01 RPC ----------- ");
        System.out.println("Id = " + id + " value = " + value);
        return "Response NAME!!!!";
    }

    public boolean test02() {
        System.out.println("------------ test02 RPC ----------- ");
        return false;
    }

    public Map<String, String> test03() {
        System.out.println("------------ test03 RPC ----------- ");
        return Collections.singletonMap("Key", "Value");
    }
}
