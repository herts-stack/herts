package org.hertsstack.e2etest.common;

import io.grpc.Metadata;
import org.hertsstack.rpc.GrpcServerOption;

public class Constant {
    public static Metadata.Key<String> HEADER_TEST01 = Metadata.Key.of("TEST", Metadata.ASCII_STRING_MARSHALLER);
    public static int port = 9999;
    public static GrpcServerOption getGrpcServerOption() {
        GrpcServerOption grpcServerOption = new GrpcServerOption();
        grpcServerOption.setPort(port);
        return grpcServerOption;
    }
}
