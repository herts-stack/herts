package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscore.HertsCoreInterceptor;
import com.tomoyane.herts.hertshttp.HertsHttpInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;

import java.util.logging.Logger;

import static com.tomoyane.herts.Constant.HEADER_TEST01;
import static com.tomoyane.herts.hertscommon.context.HertsHeaderContext.HERTS_HEADER_KEY;

public class HttpServerInterceptor implements HertsHttpInterceptor {
    private static final Logger logger = HertsLogger.getLogger(HttpServerInterceptor.class.getSimpleName());

    @Override
    public void beforeHandle() {
        logger.info("Before trigger");
    }

    @Override
    public void afterHandle() {
        logger.info("After trigger");
    }
}
