package org.hertsstack.example.jwthttp;

import org.hertsstack.core.service.HertsServiceHttp;

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
