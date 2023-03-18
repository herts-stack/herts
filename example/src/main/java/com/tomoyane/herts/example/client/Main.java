package com.tomoyane.herts.example.client;

import com.tomoyane.herts.example.UnaryRpcService;
import com.tomoyane.herts.example.UnaryRpcServiceImpl;
import com.tomoyane.herts.hertsclient.handlers.HertsClientBlockingMethodHandler;

import io.grpc.*;

import java.io.*;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, InstantiationException, IllegalAccessException {
        try {
            ManagedChannel channel = Grpc.newChannelBuilder("localhost:9000", InsecureChannelCredentials.create())
                    .build();
            System.out.println("Create client channel");

            var stub = newHertsService(channel);
            UnaryRpcService service = (UnaryRpcService) Proxy.newProxyInstance(
                    UnaryRpcService.class.getClassLoader(),
                    new Class<?>[]{ UnaryRpcService.class },
                    stub);

            var res01 = service.test01("TEST01", "VALUE01");
            var res02 = service.test02();
            var res03 = service.test03();

            System.out.println(res01);
            System.out.println(res02);
            System.out.println(res03.get("Key"));

            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static HertsClientBlockingMethodHandler newHertsService(Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<HertsClientBlockingMethodHandler> factory =
                new io.grpc.stub.AbstractStub.StubFactory<HertsClientBlockingMethodHandler>() {
                    @java.lang.Override
                    public HertsClientBlockingMethodHandler newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new HertsClientBlockingMethodHandler(channel, callOptions, new UnaryRpcServiceImpl());
                    }
                };
        return HertsClientBlockingMethodHandler.newStub(factory, channel);
    }
}