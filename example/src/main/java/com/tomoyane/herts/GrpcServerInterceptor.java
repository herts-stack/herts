package com.tomoyane.herts;

import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscore.HertsCoreInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;

import java.util.logging.Logger;

import static com.tomoyane.herts.Constant.HEADER_TEST01;
import static com.tomoyane.herts.hertscommon.context.HertsHeaderContext.HERTS_HEADER_KEY;

public class GrpcServerInterceptor implements HertsCoreInterceptor {
    private static final Logger logger = HertsLogger.getLogger(GrpcServerInterceptor.class.getSimpleName());

    @Override
    public void setResponseMetadata(Metadata metadata) {
    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {
        logger.info("====== Before call ");
        logger.info("====== Header "
                + requestHeaders.get(HEADER_TEST01) + " " + requestHeaders.get(HERTS_HEADER_KEY));
    }
}
