package org.herts.example.jwthttp;

import org.herts.core.service.HertsServiceHttp;

public class AuthHttpServiceImpl extends HertsServiceHttp<AuthHttpService> implements AuthHttpService {
    @Override
    public String signUp(String email, String password) {
        return "ok";
    }

    @Override
    public String signIn(String email, String password) {
        return "verified";
    }
}
