package org.herts.example.jwtrpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.Status;
import org.herts.core.exception.rpc.HertsRpcErrorException;
import org.herts.example.jwthttp.JwtVerifier;
import org.herts.rpc.HertsRpcInterceptor;

public class JwtServerInterceptor implements HertsRpcInterceptor {
    private final JwtVerifier jwtProcessor;

    public JwtServerInterceptor() {
        this.jwtProcessor = new JwtVerifier();
    }

    @Override
    public void setResponseMetadata(Metadata metadata) {

    }

    @Override
    public <ReqT, RespT> void beforeCallMethod(ServerCall<ReqT, RespT> call, Metadata requestHeaders) {
        Metadata.Key<String> authorization = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        String token = requestHeaders.get(authorization);
        if (token == null || token.isEmpty()) {
            Status status = HertsRpcErrorException.StatusCode.Status16
                    .convertToGrpc(HertsRpcErrorException.StatusCode.Status16)
                    .withDescription("Unauthorized");
            call.close(status, requestHeaders);
            return;
        }
        if (this.jwtProcessor.verifyToken(token)) {
            Status status = HertsRpcErrorException.StatusCode.Status16
                    .convertToGrpc(HertsRpcErrorException.StatusCode.Status16)
                    .withDescription("Unauthorized");
            call.close(status, requestHeaders);
            return;
        }
    }
}
