package org.herts.example.jwthttp;

import org.herts.core.exception.http.HttpErrorException;
import org.herts.http.HertsHttpInterceptor;
import org.herts.http.HertsHttpRequest;

public class JwtServerInterceptor implements HertsHttpInterceptor {
    private final JwtVerifier jwtProcessor;

    public JwtServerInterceptor() {
        this.jwtProcessor = new JwtVerifier();
    }

    @Override
    public void beforeHandle(HertsHttpRequest request) {
        var token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new HttpErrorException(HttpErrorException.StatusCode.Status401, "Unauthorized");
        }
        if (!this.jwtProcessor.verifyToken(token)) {
            throw new HttpErrorException(HttpErrorException.StatusCode.Status401, "Unauthorized");
        }
    }

    @Override
    public void afterHandle() {
    }
}
