package java17.com.tomoyane.herts.example.server;

import com.tomoyane.herts.BidirectionalStreamingRpcServiceImpl;
import com.tomoyane.herts.GrpcServerInterceptor;
import com.tomoyane.herts.hertscore.engine.HertsEngineBuilder;

public class Main {
    public static void main(String[] args) {
//        var service = new UnaryRpcServiceImpl();
        var service = new BidirectionalStreamingRpcServiceImpl();
        //var service = new ServerStreamingRpcServiceImpl();
//        var service = new ClientStreamingRpcServiceImpl();
        var engine = HertsEngineBuilder.Builder
                .create()
                .addService(service, new GrpcServerInterceptor())
                .build();
        engine.start();
    }
}