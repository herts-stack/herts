package org.hertsstack.example.jwtrpc;

import org.hertsstack.core.annotation.HertsRpcService;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface AuthRpcService extends HertsService {
    String signUp(String email, String password);
    String signIn(String email, String password);
}
