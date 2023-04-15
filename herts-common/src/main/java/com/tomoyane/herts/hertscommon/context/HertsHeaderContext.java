package com.tomoyane.herts.hertscommon.context;

import io.grpc.Metadata;

/**
 * Herts shared context on server and client.
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHeaderContext {
    public static final String CODE_VERSION = "v1";
    public static final String HERTS_CONTEXT_KEY = "x-herts-version";
    public static final String HERTS_SERVER_KEY = "Server";
    public static final String HERTS_SERVER_VAL = "Herts HTTP Server";
    public static final Metadata.Key<String> HERTS_HEADER_KEY = Metadata.Key.of(HERTS_CONTEXT_KEY, Metadata.ASCII_STRING_MARSHALLER);
}
