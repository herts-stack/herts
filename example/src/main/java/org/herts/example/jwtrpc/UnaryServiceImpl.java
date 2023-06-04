package org.herts.example.jwtrpc;

import org.herts.common.service.HertsUnaryService;

public class UnaryServiceImpl extends HertsUnaryService<UnaryService> implements UnaryService {
    @Override
    public String helloWorld() {
        return "hello world";
    }
}
