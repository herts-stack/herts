package org.herts.example;

import org.herts.common.logger.HertsLogger;
import org.herts.http.HertsHttpInterceptor;

import java.util.logging.Logger;

public class HttpServerInterceptor implements HertsHttpInterceptor {
    private static final Logger logger = HertsLogger.getLogger(HttpServerInterceptor.class.getSimpleName());

    @Override
    public void beforeHandle() {
        // logger.info("Before trigger");
    }

    @Override
    public void afterHandle() {
        // logger.info("After trigger");
    }
}
