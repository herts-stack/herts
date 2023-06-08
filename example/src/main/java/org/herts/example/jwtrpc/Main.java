package org.herts.example.jwtrpc;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import org.herts.core.exception.rpc.HertsRpcErrorException;
import org.herts.rpc.HertsRpcInterceptBuilder;
import org.herts.rpc.HertsRpcServerEngine;
import org.herts.rpc.HertsRpcServerEngineBuilder;
import org.herts.rpcclient.HertsRpcClient;
import org.herts.rpcclient.HertsRpcClientBuilder;

import java.util.concurrent.Executor;

public class Main {
    public static void main(String[] args) {
        startServer();
        startClient();
        System.exit(0);
    }

    private static void startServer() {
        HertsRpcServerEngine engine = HertsRpcServerEngineBuilder.builder()
                .registerHertsRpcService(new UnaryServiceImpl(), HertsRpcInterceptBuilder.builder(new JwtServerInterceptor()).build())
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
        } catch (HertsRpcErrorException ex) {
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