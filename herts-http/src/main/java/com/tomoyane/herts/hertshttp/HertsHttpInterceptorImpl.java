package com.tomoyane.herts.hertshttp;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class HertsHttpInterceptorImpl implements Filter {
    private final HertsHttpInterceptor hertsHttpInterceptor;

    public HertsHttpInterceptorImpl(HertsHttpInterceptor hertsHttpInterceptor) {
        this.hertsHttpInterceptor = hertsHttpInterceptor;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        this.hertsHttpInterceptor.beforeHandle();
        chain.doFilter(request, response);
        this.hertsHttpInterceptor.afterHandle();
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
