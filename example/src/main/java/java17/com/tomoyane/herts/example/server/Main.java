package java17.com.tomoyane.herts.example.server;

import com.tomoyane.herts.BidirectionalStreamingRpcServiceImpl;
import com.tomoyane.herts.ClientStreamingRpcServiceImpl;
import com.tomoyane.herts.UnaryRpcServiceImpl;
import com.tomoyane.herts.hertscore.core.HertsEngineBuilder;

public class Main {
    public static void main(String[] args) {
//        var service = new UnaryRpcServiceImpl();
        var service = new BidirectionalStreamingRpcServiceImpl();
        //var service = new ServerStreamingRpcServiceImpl();
//        var service = new ClientStreamingRpcServiceImpl();
        var engine = HertsEngineBuilder.Builder.create().service(service).build();
        engine.start();
    }
}