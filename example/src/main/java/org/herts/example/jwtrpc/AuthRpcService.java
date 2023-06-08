package org.herts.example.jwtrpc;

import org.herts.core.annotation.HertsRpcService;
import org.herts.core.context.HertsType;
import org.herts.core.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface AuthRpcService extends HertsService {
    String signUp(String email, String password);
    String signIn(String email, String password);
}
