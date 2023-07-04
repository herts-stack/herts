package org.hertsstack.metrics;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.service.HertsService;

@HertsHttp
public interface TestHertsService extends HertsService {
    String test01();
}
