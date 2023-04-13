package com.tomoyane.herts.httpclient;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;

public interface HertsHttpClient {
    <T extends HertsCoreService> T createHertCoreInterface(Class<T> classType);
}
