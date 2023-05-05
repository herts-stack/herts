package org.herts.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.herts.example.ArgOperation;
import org.herts.common.context.HertsType;

public class Main {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        if (!ArgOperation.isOk(args[0])) {
            System.out.println("Failed to set args. Allowed " + String.valueOf(ArgOperation.allowedArgs));
            return;
        }

        HertsType coreType = ArgOperation.convert(args[0]);
        try {
            switch (coreType) {
                case Unary -> UnaryExample.run();
                case ClientStreaming -> ClientStreamingExample.run();
                case ServerStreaming -> ServerStreamingExample.run();
                case BidirectionalStreaming -> BiStreamingExample.run();
                case Http -> HttpExample.run();
            }
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
