package java17.com.tomoyane.herts.example.server;

import com.tomoyane.herts.ArgCollector;
import com.tomoyane.herts.BidirectionalStreamingRpcRpcServiceImpl;
import com.tomoyane.herts.ClientStreamingRpcRpcServiceImpl;
import com.tomoyane.herts.GrpcServerInterceptor;
import com.tomoyane.herts.HttpServerInterceptor;
import com.tomoyane.herts.HttpServiceImpl;
import com.tomoyane.herts.ServerStreamingRpcRpcServiceImpl;
import com.tomoyane.herts.UnaryRpcRpcServiceImpl01;
import com.tomoyane.herts.UnaryRpcRpcServiceImpl02;
import com.tomoyane.herts.hertscommon.context.HertsMetricsSetting;
import com.tomoyane.herts.hertscommon.context.HertsType;
import com.tomoyane.herts.hertsrpc.HertsRpcInterceptBuilder;
import com.tomoyane.herts.hertsrpc.engine.HertsRpcEngineBuilder;
import com.tomoyane.herts.hertsrpc.engine.HertsRpcBuilder;
import com.tomoyane.herts.hertshttp.engine.HertsHttpEngine;
import com.tomoyane.herts.hertshttp.engine.HertsHttpServer;

public class Main {
    public static void main(String[] args) {
        if (!ArgCollector.isOk(args[0])) {
            System.out.println("Failed to set args. Allowed " + String.valueOf(ArgCollector.allowedArgs));
            return;
        }

        var metrics = HertsMetricsSetting.builder().isRpsEnabled(true).isLatencyEnabled(true).build();

        HertsRpcEngineBuilder engineBuilder = HertsRpcBuilder.builder();
        HertsType coreType = ArgCollector.convert(args[0]);
        var interceptor = HertsRpcInterceptBuilder.builder(new GrpcServerInterceptor()).build();
        switch (coreType) {
            case Unary -> {
                var service01 = new UnaryRpcRpcServiceImpl01();
                var service02 = new UnaryRpcRpcServiceImpl02();

                engineBuilder.addService(service01, interceptor)
                        .addService(service02, interceptor)
                        .enableMetrics(metrics);
            }
            case ClientStreaming -> {
                var service = new ClientStreamingRpcRpcServiceImpl();
                engineBuilder.addService(service, interceptor).enableMetrics(metrics);
            }
            case ServerStreaming -> {
                var service = new ServerStreamingRpcRpcServiceImpl();
                engineBuilder.addService(service, interceptor).enableMetrics(metrics);
            }
            case BidirectionalStreaming -> {
                var service = new BidirectionalStreamingRpcRpcServiceImpl();
                engineBuilder.addService(service, interceptor).enableMetrics(metrics);
            }
            case Http -> {
                HertsHttpEngine engine = HertsHttpServer.builder()
                        .addImplementationService(new HttpServiceImpl())
                        .setInterceptor(new HttpServerInterceptor())
                        .setMetricsSetting(metrics)
                        .build();
                engine.start();
                return;
            }
        }

        var engine = engineBuilder.build();
        engine.start();
    }
}