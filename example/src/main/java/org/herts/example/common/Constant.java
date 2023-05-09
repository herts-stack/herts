package org.herts.example.common;

import io.grpc.Metadata;
import org.herts.rpc.engine.GrpcServerOption;

public class Constant {
    public static Metadata.Key<String> HEADER_TEST01 = Metadata.Key.of("TEST", Metadata.ASCII_STRING_MARSHALLER);
    public static int port = 9999;
    public static GrpcServerOption getGrpcServerOption() {
        GrpcServerOption grpcServerOption = new GrpcServerOption();
        grpcServerOption.setPort(port);
        return grpcServerOption;
    }
}
