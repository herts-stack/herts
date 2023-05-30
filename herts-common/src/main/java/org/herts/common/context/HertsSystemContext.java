package org.herts.common.context;

import io.grpc.Context;
import io.grpc.Metadata;

/**
 * Herts shared context on server and client.
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsSystemContext {

    public static class Header {
        public static final String CODE_VERSION = "v1";
        public static final String HERTS_CONTEXT_VERSION = "x-herts-version";
        public static final String HERTS_CONTEXT_CLIENT_ID = "x-client-id";
        public static final String HERTS_SERVER_KEY = "Server";
        public static final String HERTS_SERVER_VAL = "Herts HTTP Server";

        public static final Context.Key<String> HERTS_CONNECTION_ID_CTX = Context.key(HERTS_CONTEXT_CLIENT_ID);

        public static final Metadata.Key<String> HERTS_VERSION = Metadata.Key.of(HERTS_CONTEXT_VERSION, Metadata.ASCII_STRING_MARSHALLER);
        public static final Metadata.Key<String> HERTS_CONNECTION_ID = Metadata.Key.of(HERTS_CONTEXT_CLIENT_ID, Metadata.ASCII_STRING_MARSHALLER);
    }

    public static class Rpc {
        public static final String METHOD_NAME = "MethodName";
        public static final String RECEIVER_METHOD_NAME = "registerReceiver";
        public static final String REGISTERED_METHOD_NAME = "registered";
    }
}
