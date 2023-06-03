package org.herts.example.jwthttp;

import org.herts.common.exception.http.HertsHttpErrorException;
import org.herts.http.HertsHttpInterceptor;
import org.herts.http.http.HertsHttpRequest;

public class JwtServerInterceptor implements HertsHttpInterceptor {
    private final JwtVerifier jwtProcessor;

    public JwtServerInterceptor() {
        this.jwtProcessor = new JwtVerifier();
    }

    @Override
    public void beforeHandle(HertsHttpRequest request) {
        var token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new HertsHttpErrorException(HertsHttpErrorException.StatusCode.Status401, "Unauthorized");
        }
        if (!this.jwtProcessor.verifyToken(token)) {
            throw new HertsHttpErrorException(HertsHttpErrorException.StatusCode.Status401, "Unauthorized");
        }
    }

    @Override
    public void afterHandle() {
    }
}
