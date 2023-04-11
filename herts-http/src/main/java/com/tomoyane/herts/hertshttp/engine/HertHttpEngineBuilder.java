package com.tomoyane.herts.hertshttp.engine;

import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.hertshttp.HertsHttpInterceptor;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public interface HertHttpEngineBuilder {
    HertHttpEngineBuilder setInterceptor(HertsHttpInterceptor interceptor);
    HertHttpEngineBuilder addService(HertsCoreService hertsCoreService);
    HertHttpEngineBuilder setPort(int port);
    HertHttpEngineBuilder setSsl(SslContextFactory sslContextFactory, int port);
    HertsHttpEngine build();
}
