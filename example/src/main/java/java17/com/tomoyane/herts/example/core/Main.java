package java17.com.tomoyane.herts.example.core;

import com.tomoyane.herts.ClientStreamingRpcServiceImpl;
import com.tomoyane.herts.hertscore.core.HertsEngine;

public class Main {
    public static void main(String[] args) {
//        var service = new UnaryRpcServiceImpl();
//        var service = new BidirectionalStreamingRpcServiceImpl();
        //var service = new ServerStreamingRpcServiceImpl();
        var service = new ClientStreamingRpcServiceImpl();
        var engine = new HertsEngine();
        engine.register(service);
        engine.start();
    }
}