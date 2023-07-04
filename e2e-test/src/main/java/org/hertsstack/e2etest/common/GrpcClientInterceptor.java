package org.hertsstack.e2etest.common;

import org.hertsstack.rpcclient.HertsRpcClientInterceptor;
import org.hertsstack.core.logger.Logging;
import io.grpc.CallOptions;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Channel;

public class GrpcClientInterceptor implements HertsRpcClientInterceptor {
    private static final java.util.logging.Logger logger = Logging.getLogger(GrpcClientInterceptor.class.getSimpleName());

    @Override
    public void setRequestMetadata(Metadata metadata) {
        metadata.put(Constant.HEADER_TEST01, "TEST_VALUE");
    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
//        logger.info("====== Before call");
    }
}