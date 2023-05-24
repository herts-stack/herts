package org.herts.http;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.herts.http.http.HertsHttpRequestImpl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http intercept
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpInterceptHandler implements Filter {
    private final ConcurrentMap<String, HertsHttpInterceptor> interceptors;

    public HertsHttpInterceptHandler(Map<String, HertsHttpInterceptor> interceptorMap) {
        this.interceptors = new ConcurrentHashMap<>(interceptorMap);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        String serviceName = parseUri(httpReq.getRequestURI());
        HertsHttpInterceptor intercept = this.interceptors.get(serviceName);
        if (intercept != null) {
            intercept.beforeHandle(new HertsHttpRequestImpl(request, httpReq));
        }
        chain.doFilter(request, response);
        if (intercept != null) {
            intercept.afterHandle();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private String parseUri(String uri) {
        try {
            var splitUris = uri.split("/");
            if (splitUris.length < 2) {
                return "";
            } else {
                return splitUris[2];
            }
        } catch (Exception ex) {
            return "";
        }
    }
}
