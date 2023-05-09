package org.herts.example.server;

import org.herts.common.logger.HertsLogger;
import org.herts.http.HertsHttpInterceptor;
import org.herts.http.http.HertsHttpRequest;

import java.util.logging.Logger;

public class HttpServerInterceptor implements HertsHttpInterceptor {
    private static final Logger logger = HertsLogger.getLogger(HttpServerInterceptor.class.getSimpleName());

    @Override
    public void beforeHandle(HertsHttpRequest request) {
        logger.info("Intercept before " + request.getHeader("Authorization"));
    }

    @Override
    public void afterHandle() {
        logger.info("Intercept after ");
    }
}
