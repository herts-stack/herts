package org.herts.example;

import io.grpc.Metadata;

public class Constant {
    public static Metadata.Key<String> HEADER_TEST01 = Metadata.Key.of("TEST", Metadata.ASCII_STRING_MARSHALLER);
}
