package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;

public class ArgCollector {
    public static char[] allowedArgs = new char[] {'U', 'u', 'C', 'c', 'S', 's', 'B', 'b'};
    public static boolean isOk(String arg) {
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

    public static HertsCoreType convert(String arg) {
        char argChar = arg.charAt(0);
        if (argChar == allowedArgs[0] || argChar == allowedArgs[1]) {
            return HertsCoreType.Unary;
        }
        if (argChar == allowedArgs[2] || argChar == allowedArgs[3]) {
            return HertsCoreType.ClientStreaming;
        }
        if (argChar == allowedArgs[4] || argChar == allowedArgs[5]) {
            return HertsCoreType.ServerStreaming;
        }
        if (argChar == allowedArgs[6] || argChar == allowedArgs[7]) {
            return HertsCoreType.BidirectionalStreaming;
        }
        return HertsCoreType.Unary;
    }
}
