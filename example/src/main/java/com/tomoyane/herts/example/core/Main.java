package com.tomoyane.herts.example.core;

import com.tomoyane.herts.example.StreamingRpcServiceImpl;
import com.tomoyane.herts.example.UnaryRpcServiceImpl;
import com.tomoyane.herts.hertscore.core.HertsEngine;

public class Main {
    public static void main(String[] args) {
        var service = new UnaryRpcServiceImpl();
//        var service = new StreamingRpcServiceImpl();
        var engine = new HertsEngine();
        engine.register(service);
        engine.start();
    }
}