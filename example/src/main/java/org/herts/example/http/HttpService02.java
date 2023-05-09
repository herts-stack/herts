package org.herts.example;

import org.herts.common.annotation.HertsHttp;
import org.herts.common.service.HertsService;

import java.util.Map;

@HertsHttp
public interface HttpService02 extends HertsService {
    String httpTest10();
}
