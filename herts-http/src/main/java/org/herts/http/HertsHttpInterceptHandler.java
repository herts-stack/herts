package org.herts.http;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import org.herts.common.exception.http.HertsHttpErrorException;
import org.herts.common.modelx.HertsHttpErrorResponse;
import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.http.http.HertsHttpRequestImpl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http intercept
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpInterceptHandler implements Filter {
    private final ConcurrentMap<String, HertsHttpInterceptor> interceptors;
    private final HertsSerializer hertsSerializer;

    public HertsHttpInterceptHandler(Map<String, HertsHttpInterceptor> interceptorMap) {
        this.interceptors = new ConcurrentHashMap<>(interceptorMap);
        this.hertsSerializer = new HertsSerializer(HertsSerializeType.Json);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        String serviceName = parseUri(httpReq.getRequestURI());
        HertsHttpInterceptor intercept = this.interceptors.get(serviceName);
        if (intercept != null) {
            try {
                intercept.beforeHandle(new HertsHttpRequestImpl(request, httpReq));
            } catch (HertsHttpErrorException ex) {
                setError(ex.getMessage(), ex.getStatusCode().getIntegerCode(), ex.getStatusCode(), (HttpServletResponse) response);
                return;
            } catch (Exception ex) {
                setError(ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, HertsHttpErrorException.StatusCode.Status500, (HttpServletResponse) response);
                return;
            }
        }
        chain.doFilter(request, response);
        if (intercept != null) {
            intercept.afterHandle();
        }
    }

    private void setError(String message, int statusCode, HertsHttpErrorException.StatusCode statusCodeEnum, HttpServletResponse response) throws IOException {
        HertsHttpErrorResponse errorResponse = HertsHttpServerCore.genErrorResponse(statusCodeEnum, message);
        response.setStatus(statusCode);
        HertsHttpCallerBase.setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(errorResponse));
    }

    @Override
    public void destroy() {
    }

    private String parseUri(String uri) {
        try {
            String[] splitUris = uri.split("/");
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
