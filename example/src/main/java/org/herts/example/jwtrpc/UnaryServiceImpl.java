package org.herts.example.jwtrpc;

import org.herts.core.service.HertsServiceUnary;

public class UnaryServiceImpl extends HertsServiceUnary<UnaryService> implements UnaryService {
    @Override
    public String helloWorld() {
        return "hello world";
    }
}
