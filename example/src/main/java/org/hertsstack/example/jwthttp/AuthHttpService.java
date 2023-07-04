package org.hertsstack.example.jwthttp;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.service.HertsService;

@HertsHttp
public interface AuthHttpService extends HertsService {
    String signUp(String email, String password);
    String signIn(String email, String password);
}
