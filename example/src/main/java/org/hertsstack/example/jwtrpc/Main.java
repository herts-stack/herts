package org.hertsstack.example.jwtrpc;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.ServerInterceptor;
import org.hertsstack.core.exception.rpc.RpcErrorException;
import org.hertsstack.rpc.HertsRpcInterceptBuilder;
import org.hertsstack.rpc.HertsRpcServerEngine;
import org.hertsstack.rpc.HertsRpcServerEngineBuilder;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;

import java.util.concurrent.Executor;

public class Main {
    public static void main(String[] args) {
        startServer();
        startClient();
        System.exit(0);
    }

    private static void startServer() {
        ServerInterceptor interceptor = HertsRpcInterceptBuilder.builder(new JwtServerInterceptor()).build();
        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsRpcService(new UnaryServiceImpl(), interceptor)
                .registerHertsRpcService(new AuthRpcServiceImpl())
                .build();

        Thread t = new Thread(engine::start);
        t.start();
    }

    private static void startClient() {
        HertsRpcClient client = HertsRpcClientBuilder
                .builder("localhost")
                .secure(false)
                .registerHertsRpcServiceInterface(UnaryService.class)
                .registerHertsRpcServiceInterface(AuthRpcService.class)
                .connect();

        UnaryService service = client.createHertsRpcService(UnaryService.class);
        try {
            service.helloWorld();
        } catch (RpcErrorException ex) {
            System.out.println("Error on client: " + ex.getMessage());
        }

        AuthRpcService authService = client.createHertsRpcService(AuthRpcService.class);
        var token = authService.signIn("email", "password");
        System.out.println("Received token on client: " + token);

        service = client.createHertsRpcService(UnaryService.class, generateCredential(token));
        var res = service.helloWorld();
        System.out.println("Received data on client: " + res);
    }

    private static CallCredentials generateCredential(String token) {
        return new CallCredentials() {
            @Override
            public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
                Metadata metadata = new Metadata();
                metadata.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + token);
                applier.apply(metadata);
            }

            @Override
            public void thisUsesUnstableApi() {
            }
        };
    }
}