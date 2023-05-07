package org.herts.metrics;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

import java.util.List;
import java.util.Map;

@HertsHttp
public interface TestHertsService extends HertsService {
    String test01();
}
