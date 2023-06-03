package org.herts.example.jwthttp;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

@HertsHttp
public interface AuthService extends HertsService {
    String signUp(String email, String password);
    String signIn(String email, String password);
}
