package com.tomoyane.herts.hertscommon.context;

import io.grpc.Metadata;

public class HertsHeaderContext {
    public static final String CODE_VERSION = "v1";
    public static final Metadata.Key<String> HERTS_HEADER_KEY =
            Metadata.Key.of("x-herts-client-version", Metadata.ASCII_STRING_MARSHALLER);
}
