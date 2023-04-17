package com.tomoyane.herts;

import com.tomoyane.herts.hertscoreclient.HertCoreClientInterceptor;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import io.grpc.CallOptions;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Channel;

import java.util.logging.Logger;

import static com.tomoyane.herts.Constant.HEADER_TEST01;

public class GrpcClientInterceptor implements HertCoreClientInterceptor {
    private static final Logger logger = HertsLogger.getLogger(GrpcClientInterceptor.class.getSimpleName());

    @Override
    public void setRequestMetadata(Metadata metadata) {
        metadata.put(HEADER_TEST01, "TEST_VALUE");
    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
//        logger.info("====== Before call");
    }
}