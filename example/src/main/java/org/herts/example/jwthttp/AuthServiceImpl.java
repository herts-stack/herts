package org.herts.example.jwthttp;

import org.herts.common.service.HertsHttpService;

public class AuthServiceImpl extends HertsHttpService<AuthService> implements AuthService {
    @Override
    public String signUp(String email, String password) {
        return "ok";
    }

    @Override
    public String signIn(String email, String password) {
        return "verified";
    }
}
