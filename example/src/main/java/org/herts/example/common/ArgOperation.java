package org.herts.example.common;

import org.herts.common.context.HertsType;

public class ArgOperation {
    public static char[] allowedArgs = new char[] {'U', 'u', 'C', 'c', 'S', 's', 'B', 'b', 'h', 'H', 'd', 'D'};
    public static boolean isHertsTypeOk(String arg) {
        if (arg == null || arg.isEmpty()) {
            return false;
        }
        char argChar = arg.charAt(0);
        for (char c : allowedArgs) {
            if (c == argChar) {
                return true;
            }
        }
        return false;
    }

    public static final String SERVER = "server";
    public static final String CLIENT = "client";
    public static boolean isExecuteTypeOk(String arg) {
        if (arg == null || arg.isEmpty()) {
            return false;
        }
        if (!arg.equals(SERVER) && !arg.equals(CLIENT)) {
            return false;
        }
        return true;
    }

    public static HertsType convert(String arg) {
        char argChar = arg.charAt(0);
        if (argChar == allowedArgs[0] || argChar == allowedArgs[1]) {
            return HertsType.Unary;
        }
        if (argChar == allowedArgs[2] || argChar == allowedArgs[3]) {
            return HertsType.ClientStreaming;
        }
        if (argChar == allowedArgs[4] || argChar == allowedArgs[5]) {
            return HertsType.ServerStreaming;
        }
        if (argChar == allowedArgs[6] || argChar == allowedArgs[7]) {
            return HertsType.BidirectionalStreaming;
        }
        if (argChar == allowedArgs[8] || argChar == allowedArgs[9]) {
            return HertsType.Http;
        }
        if (argChar == allowedArgs[10] || argChar == allowedArgs[11]) {
            return HertsType.Reactive;
        }
        return HertsType.Unary;
    }
}
