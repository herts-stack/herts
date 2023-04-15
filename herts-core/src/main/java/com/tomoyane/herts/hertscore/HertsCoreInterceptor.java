package com.tomoyane.herts.hertscore;

import io.grpc.Metadata;
import io.grpc.ServerCall;

/**
 * Herts server interceptor
 * @author Herts Contributer
 * @version 1.0.0
 */
public interface HertsCoreInterceptor {

    /**
     * Set response grpc metadata
     * @param metadata Metadata
     */
    void setResponseMetadata(Metadata metadata);

    /**
     * Beforee intercept
     * @param call ServerCall
     * @param requestHeaders Metadata
     * @param <ReqT> Request generics
     * @param <RespT> Response generics
     */
    <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, final Metadata requestHeaders);
}
