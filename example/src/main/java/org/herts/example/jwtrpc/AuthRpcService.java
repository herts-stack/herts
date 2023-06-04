package org.herts.example.jwtrpc;

import org.herts.common.annotation.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.service.HertsService;

@HertsRpcService(value = HertsType.Unary)
public interface AuthRpcService extends HertsService {
    String signUp(String email, String password);
    String signIn(String email, String password);
}
