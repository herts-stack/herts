package org.herts.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.herts.example.bidstreaming_rpc.client.BiStreamingClient;
import org.herts.example.clientstreaming_rpc.client.ClientStreamingClient;
import org.herts.example.common.ArgOperation;
import org.herts.common.context.HertsType;
import org.herts.example.bidstreaming_rpc.server.BiStreamingServer;
import org.herts.example.clientstreaming_rpc.server.ClientStreamingServer;
import org.herts.example.http.client.HttpClient;
import org.herts.example.http.server.HttpServer;
import org.herts.example.serverstreaming_rpc.client.ServerStreamingClient;
import org.herts.example.serverstreaming_rpc.server.ServerStreamingServer;
import org.herts.example.unary_rpc.client.UnaryClient;
import org.herts.example.unary_rpc.server.UnaryServer;

public class Main {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        if (!ArgOperation.isExecuteTypeOk(args[0])) {
            System.out.println("Failed to set first args. Allowed `server` or `client` only");
            return;
        }
        if (!ArgOperation.isHertsTypeOk(args[1])) {
            System.out.println("Failed to set second args. Allowed " + String.valueOf(ArgOperation.allowedArgs));
            return;
        }

        HertsType coreType = ArgOperation.convert(args[1]);
        if (args[0].equals(ArgOperation.SERVER)) {
            switch (coreType) {
                case Unary -> {
                    UnaryServer.run();
                }
                case ClientStreaming -> {
                    ClientStreamingServer.run();
                }
                case ServerStreaming -> {
                    ServerStreamingServer.run();
                }
                case BidirectionalStreaming -> {
                    BiStreamingServer.run();
                }
                case Http -> {
                    HttpServer.run();
                }
            }
        } else {
            try {
                switch (coreType) {
                    case Unary -> UnaryClient.run();
                    case ClientStreaming -> ClientStreamingClient.run();
                    case ServerStreaming -> ServerStreamingClient.run();
                    case BidirectionalStreaming -> BiStreamingClient.run();
                    case Http -> HttpClient.run();
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}