package org.herts.example.server;

import org.herts.example.ArgOperation;
import org.herts.common.context.HertsType;

public class Main {
    public static void main(String[] args) {
        if (!ArgOperation.isOk(args[0])) {
            System.out.println("Failed to set args. Allowed " + String.valueOf(ArgOperation.allowedArgs));
            return;
        }

        HertsType coreType = ArgOperation.convert(args[0]);
        switch (coreType) {
            case Unary -> {
                UnaryExample.run();
            }
            case ClientStreaming -> {
                ClientStreamingExample.run();
            }
            case ServerStreaming -> {
                ServerStreamingExample.run();
            }
            case BidirectionalStreaming -> {
                BiStreamingExample.run();
            }
            case Http -> {
                HttpExample.run();
                return;
            }
        }
    }
}