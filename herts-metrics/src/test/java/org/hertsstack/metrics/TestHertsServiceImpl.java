package org.hertsstack.metrics;

import org.hertsstack.core.service.HertsServiceHttp;

public class TestHertsServiceImpl extends HertsServiceHttp<TestHertsService> implements TestHertsService {
    @Override
    public String test01() {
        return "test01";
    }
}
