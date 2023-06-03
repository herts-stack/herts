package org.herts.example.jwthttp;

import org.herts.http.HertsHttpInterceptor;
import org.herts.http.http.HertsHttpRequest;

public class JwtServerInterceptor implements HertsHttpInterceptor {

    @Override
    public void beforeHandle(HertsHttpRequest request) {
    }

    @Override
    public void afterHandle() {
    }
}
