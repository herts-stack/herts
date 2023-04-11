package com.tomoyane.herts.hertshttp;

public interface HertsHttpInterceptor {
    void beforeHandle();
    void afterHandle();
}
