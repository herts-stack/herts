package java17.com.tomoyane.herts.example.server;

import com.tomoyane.herts.ArgCollector;
import com.tomoyane.herts.BidirectionalStreamingRpcCoreServiceImpl;
import com.tomoyane.herts.ClientStreamingRpcCoreServiceImpl;
import com.tomoyane.herts.GrpcServerInterceptor;
import com.tomoyane.herts.HttpServerInterceptor;
import com.tomoyane.herts.HttpServiceImpl;
import com.tomoyane.herts.ServerStreamingRpcCoreServiceImpl;
import com.tomoyane.herts.UnaryRpcCoreServiceImpl01;
import com.tomoyane.herts.UnaryRpcCoreServiceImpl02;
import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscore.HertsCoreInterceptorBuilderImpl;
import com.tomoyane.herts.hertscore.engine.HertsCoreEngineBuilder;
import com.tomoyane.herts.hertscore.engine.HertsCoreEngineImpl;
import com.tomoyane.herts.hertshttp.engine.HertsHttpEngine;
import com.tomoyane.herts.hertshttp.engine.HertsHttpEngineImpl;

public class Main {
    public static void main(String[] args) {
        if (!ArgCollector.isOk(args[0])) {
            System.out.println("Failed to set args. Allowed " + String.valueOf(ArgCollector.allowedArgs));
            return;
        }

        HertsCoreEngineBuilder engineBuilder = HertsCoreEngineImpl.Builder.create();
        HertsCoreType coreType = ArgCollector.convert(args[0]);
        var interceptor = HertsCoreInterceptorBuilderImpl.Builder.create(new GrpcServerInterceptor()).build();
        switch (coreType) {
            case Unary -> {
                var service01 = new UnaryRpcCoreServiceImpl01();
                var service02 = new UnaryRpcCoreServiceImpl02();

                engineBuilder.addService(service01, interceptor).addService(service02, interceptor);
            }
            case ClientStreaming -> {
                var service = new ClientStreamingRpcCoreServiceImpl();
                engineBuilder.addService(service, interceptor);
            }
            case ServerStreaming -> {
                var service = new ServerStreamingRpcCoreServiceImpl();
                engineBuilder.addService(service, interceptor);
            }
            case BidirectionalStreaming -> {
                var service = new BidirectionalStreamingRpcCoreServiceImpl();
                engineBuilder.addService(service, interceptor);
            }
            case Http -> {
                HertsHttpEngine engine = HertsHttpEngineImpl.Builder.create()
                        .addServiceImplementation(new HttpServiceImpl())
                        .setInterceptor(new HttpServerInterceptor())
                        .build();
                engine.start();
                return;
            }
        }

        var engine = engineBuilder.build();
        engine.start();
    }
}