package org.hertsstack.rpcclient;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import org.hertsstack.core.context.SharedServiceContext;

import java.util.UUID;
import java.util.concurrent.Executor;

class ClientRequestInfo extends CallCredentials {
    private final String clientId;

    public static ClientRequestInfo create() {
        return new ClientRequestInfo();
    }

    public ClientRequestInfo() {
        this.clientId = UUID.randomUUID().toString();
    }

    public ClientRequestInfo(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier applier) {
        executor.execute(new Runnable() {
            @Override public void run() {
                Metadata headers = new Metadata();
                Metadata.Key<String> clientIdKey = SharedServiceContext.Header.HERTS_CONNECTION_ID;
                headers.put(clientIdKey, clientId);
                Metadata.Key<String> versionKey = SharedServiceContext.Header.HERTS_VERSION;
                headers.put(versionKey, SharedServiceContext.Header.CODE_VERSION);
                applier.apply(headers);
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {

    }
}
