package org.hertsstack.http;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hertsstack.core.exception.http.HttpErrorException;
import org.hertsstack.core.modelx.InternalHttpErrorResponse;
import org.hertsstack.serializer.MessageSerializeType;
import org.hertsstack.serializer.MessageSerializer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http intercept
 *
 * @author Herts Contributer
 */
public class InternalHttpInterceptHandler implements Filter {
    private final ConcurrentMap<String, HertsHttpInterceptor> interceptors;
    private final MessageSerializer hertsSerializer;
    private final boolean isApiServer;

    public InternalHttpInterceptHandler(Map<String, HertsHttpInterceptor> interceptorMap, boolean isApiServer) {
        if (interceptorMap == null) {
            this.interceptors = new ConcurrentHashMap<>();
        } else {
            this.interceptors = new ConcurrentHashMap<>(interceptorMap);
        }
        this.hertsSerializer = new MessageSerializer(MessageSerializeType.Json);
        this.isApiServer = isApiServer;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        String serviceName = parseUri(httpReq.getRequestURI());
        HertsHttpCallerBase.setHertsServerDefaultHeader(httpRes, this.isApiServer);

        HertsHttpInterceptor intercept = this.interceptors.get(serviceName);
        if (intercept != null) {
            try {
                intercept.beforeHandle(new HertsHttpRequestImpl(request, httpReq));
            } catch (HttpErrorException ex) {
                setError(ex.getMessage(), ex.getStatusCode().getIntegerCode(), ex.getStatusCode(), httpRes);
                return;
            } catch (Exception ex) {
                setError(ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, HttpErrorException.StatusCode.Status500, httpRes);
                return;
            }
        }
        chain.doFilter(request, response);
        if (intercept != null) {
            try {
                intercept.afterHandle(new HertsHttpResponseImpl(httpRes));
            } catch (Exception ex) {
                setError(ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, HttpErrorException.StatusCode.Status500, httpRes);
            }
        }
    }

    private void setError(String message, int statusCode, HttpErrorException.StatusCode statusCodeEnum, HttpServletResponse response) throws IOException {
        InternalHttpErrorResponse errorResponse = InternalHttpServlet.genErrorResponse(statusCodeEnum, message);
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
