package org.herts.example.jwthttp;

import org.herts.core.annotation.HertsHttp;
import org.herts.core.service.HertsService;

@HertsHttp
public interface AuthHttpService extends HertsService {
    String signUp(String email, String password);
    String signIn(String email, String password);
}
