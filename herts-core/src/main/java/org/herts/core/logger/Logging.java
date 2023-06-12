package org.herts.core.logger;

import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Herts logger
 * Wrapped java.util.logging.Logger
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class Logging {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$s %2$s %5$s%6$s%n");

        System.setProperty("java.util.logging.manager", Manager.class.getName());

        Handler stdoutHandler = new StreamHandler(System.out, new SimpleFormatter());
        Handler stderrHandler = new StreamHandler(System.err, new SimpleFormatter());
        java.util.logging.Logger.getAnonymousLogger().addHandler(stdoutHandler);
        java.util.logging.Logger.getAnonymousLogger().addHandler(stderrHandler);
    }

    /**
     * Get java.util.logging.Logger
     *
     * @param loggerName logger name
     * @return java.util.logging.Logger
     */
    public static java.util.logging.Logger getLogger(String loggerName) {
        return java.util.logging.Logger.getAnonymousLogger();
    }

    /**
     * Custom LogManager.
     */
    public static class Manager extends LogManager {
        static Manager instance;

        public Manager() {
            instance = this;
        }

        @Override
        public void reset() {
        }

        private void reset0() {
            super.reset();
        }

        public static void resetFinally() {
            instance.reset0();
        }
    }
}
