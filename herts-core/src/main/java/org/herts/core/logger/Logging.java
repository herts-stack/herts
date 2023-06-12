package org.herts.core.logger;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Herts logger
 * Wrapped java.util.logging.Logger
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class Logging {
    public static java.util.logging.Logger LOGGER;
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$s %2$s %5$s%6$s%n");

        System.setProperty("java.util.logging.manager", Manager.class.getName());

        Handler stdoutHandler = new StdoutConsoleHandler();
        stdoutHandler.setLevel(Level.INFO);

        Handler stderrHandler = new StderrConsoleHandler();
        stderrHandler.setLevel(Level.WARNING);

        LOGGER = java.util.logging.Logger.getAnonymousLogger();
        LOGGER.addHandler(stdoutHandler);
        LOGGER.addHandler(stderrHandler);
        LOGGER.setUseParentHandlers(false);

        stdoutHandler.setFilter(record -> record.getLevel().intValue() < Level.WARNING.intValue());
        stderrHandler.setFilter(record -> record.getLevel().intValue() >= Level.WARNING.intValue());
    }

    static class StderrConsoleHandler extends ConsoleHandler {
        protected void setOutputStream(OutputStream out) throws SecurityException {
            super.setOutputStream(System.err);
        }
    }

    static class StdoutConsoleHandler extends ConsoleHandler {
        protected void setOutputStream(OutputStream out) throws SecurityException {
            super.setOutputStream(System.out);
        }
    }

    /**
     * Get java.util.logging.Logger
     *
     * @param loggerName logger name
     * @return java.util.logging.Logger
     */
    public static java.util.logging.Logger getLogger(String loggerName) {
        return LOGGER;
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
