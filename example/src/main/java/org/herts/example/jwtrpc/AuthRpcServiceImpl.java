package org.herts.example.jwtrpc;

import org.herts.core.service.HertsServiceUnary;

public class AuthRpcServiceImpl extends HertsServiceUnary<AuthRpcService> implements AuthRpcService {
    @Override
    public String signUp(String email, String password) {
        return "ok";
    }

    @Override
    public String signIn(String email, String password) {
        return "verified";
    }
}
