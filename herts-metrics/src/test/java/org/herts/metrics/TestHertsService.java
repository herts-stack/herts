package org.herts.metrics;

import org.herts.core.annotation.HertsHttp;
import org.herts.core.service.HertsService;

@HertsHttp
public interface TestHertsService extends HertsService {
    String test01();
}
