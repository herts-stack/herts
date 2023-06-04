package org.herts.example.jwthttp;

import org.herts.common.service.HertsHttpService;

public class AuthHttpServiceImpl extends HertsHttpService<AuthHttpService> implements AuthHttpService {
    @Override
    public String signUp(String email, String password) {
        return "ok";
    }

    @Override
    public String signIn(String email, String password) {
        return "verified";
    }
}