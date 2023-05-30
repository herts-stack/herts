package org.herts.e2etest;

import org.herts.common.context.HertsType;

public class ArgOperation {
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

    public static String[] allowedHertsTypes = new String[]{
            "unary", "server_streaming", "client_streaming", "bidirectional_streaming", "reactive_streaming", "http"
    };
    public static boolean isHertsTypeOk(String arg) {
        if (!arg.equals(allowedHertsTypes[0]) &&
                !arg.equals(allowedHertsTypes[1]) &&
                !arg.equals(allowedHertsTypes[2]) &&
                !arg.equals(allowedHertsTypes[3]) &&
                !arg.equals(allowedHertsTypes[4]) &&
                !arg.equals(allowedHertsTypes[5])) {
            return false;
        }
        return true;
    }
    public static String[] testTypes = new String[]{
            "integration", "reactive_queue"
    };
    public static boolean isTestTypeOk(String arg) {
        if (!arg.equals(testTypes[0]) &&
                !arg.equals(testTypes[1])) {
            return false;
        }
        return true;
    }

    public static HertsType convert(String arg) {
        if (arg.equals(allowedHertsTypes[0])) {
            return HertsType.Unary;
        }
        if (arg.equals(allowedHertsTypes[1])) {
            return HertsType.ServerStreaming;
        }
        if (arg.equals(allowedHertsTypes[2])) {
            return HertsType.ClientStreaming;
        }
        if (arg.equals(allowedHertsTypes[3])) {
            return HertsType.BidirectionalStreaming;
        }
        if (arg.equals(allowedHertsTypes[4])) {
            return HertsType.Reactive;
        }
        if (arg.equals(allowedHertsTypes[5])) {
            return HertsType.Http;
        }
        return HertsType.Unary;
    }
}
