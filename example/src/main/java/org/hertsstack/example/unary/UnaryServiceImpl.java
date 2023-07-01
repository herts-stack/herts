package org.hertsstack.example.unary;

import org.hertsstack.core.service.HertsServiceUnary;

public class UnaryServiceImpl extends HertsServiceUnary<UnaryService> implements UnaryService {
    @Override
    public String helloWorld() {
        return "hello world";
    }
}
