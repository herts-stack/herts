package org.hertsstack.example.jwthttp;

import org.hertsstack.core.exception.http.HttpErrorException;
import org.hertsstack.http.HertsHttpInterceptor;
import org.hertsstack.http.HertsHttpRequest;

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
