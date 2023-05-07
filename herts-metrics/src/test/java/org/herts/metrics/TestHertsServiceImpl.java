package org.herts.metrics;

import org.herts.common.service.HertsHttpService;

public class TestHertsServiceImpl extends HertsHttpService<TestHertsService> implements TestHertsService {
    @Override
    public String test01() {
        return "test01";
    }
}
