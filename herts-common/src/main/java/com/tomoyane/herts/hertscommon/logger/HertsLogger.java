package com.tomoyane.herts.hertscommon.logger;

public class HertsLogger {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$s %2$s %5$s%6$s%n");
    }

    public static java.util.logging.Logger getLogger(String loggerName) {
        return java.util.logging.Logger.getLogger(loggerName);
    }
}
