package org.herts.rpcclient.modelx;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import org.herts.common.context.HertsSystemContext;

import java.util.UUID;
import java.util.concurrent.Executor;

public class ClientConnection extends CallCredentials {
    private final String clientId;

    public static ClientConnection create() {
        return new ClientConnection();
    }

    public ClientConnection() {
        this.clientId = UUID.randomUUID().toString();
    }

    public ClientConnection(String clientId) {
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
                Metadata.Key<String> clientIdKey = HertsSystemContext.Header.HERTS_CONNECTION_ID;
                headers.put(clientIdKey, clientId);
                Metadata.Key<String> versionKey = HertsSystemContext.Header.HERTS_VERSION;
                headers.put(versionKey, HertsSystemContext.Header.CODE_VERSION);
                applier.apply(headers);
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {

    }
}
