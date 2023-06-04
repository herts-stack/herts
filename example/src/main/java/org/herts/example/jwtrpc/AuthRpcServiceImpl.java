package org.herts.example.jwtrpc;

import org.herts.common.service.HertsUnaryService;

public class AuthRpcServiceImpl extends HertsUnaryService<AuthRpcService> implements AuthRpcService {
    @Override
    public String signUp(String email, String password) {
        return "ok";
    }

    @Override
    public String signIn(String email, String password) {
        return "verified";
    }
}
