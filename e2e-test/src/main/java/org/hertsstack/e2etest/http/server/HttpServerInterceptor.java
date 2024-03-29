package org.hertsstack.e2etest.http.server;

import org.hertsstack.core.logger.Logging;
import org.hertsstack.http.HertsHttpInterceptor;
import org.hertsstack.http.HertsHttpRequest;
import org.hertsstack.http.HertsHttpResponse;

public class HttpServerInterceptor implements HertsHttpInterceptor {
    private static final java.util.logging.Logger logger = Logging.getLogger(HttpServerInterceptor.class.getSimpleName());

    @Override
    public void beforeHandle(HertsHttpRequest request) {
        logger.info("Intercept before " + request.getHeader("Authorization"));
    }

    @Override
    public void afterHandle(HertsHttpResponse response) {
        logger.info("Intercept after ");
    }
}
