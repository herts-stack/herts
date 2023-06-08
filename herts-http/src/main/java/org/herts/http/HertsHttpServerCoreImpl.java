package org.herts.http;

import org.herts.core.exception.HertsJsonProcessingException;
import org.herts.core.modelx.HertsHttpErrorResponse;
import org.herts.core.exception.HertsInvalidBodyException;
import org.herts.core.exception.http.HertsHttpErrorException;
import org.herts.core.logger.HertsLogger;
import org.herts.core.serializer.HertsSerializeType;
import org.herts.core.serializer.HertsSerializer;
import org.herts.core.service.HertsService;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.HertsMetricsServer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Herts http server core
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsHttpServerCoreImpl extends HttpServlet implements HertsHttpServerCore {
    private static final Logger logger = HertsLogger.getLogger(HertsHttpServerCoreImpl.class.getSimpleName());

    private final HertsMetrics hertsHttpMetrics;
    private final HertsHttpCaller hertsHttpCaller;
    private final HertsSerializer hertsSerializer = new HertsSerializer(HertsSerializeType.Json);
    private final ConcurrentMap<String, Method> methods = new ConcurrentHashMap<>();
    private final String baseEndpoint;

    public void init() {
    }

    public HertsHttpServerCoreImpl(HertsService hertsRpcService, HertsMetrics hertsHttpMetrics, HertsMetricsServer metricsServer)
            throws ClassNotFoundException, NoSuchMethodException {

        String serviceName = hertsRpcService.getClass().getInterfaces()[0].getSimpleName();
        this.hertsHttpMetrics = hertsHttpMetrics;
        this.hertsHttpMetrics.register();
        this.baseEndpoint = "/api/" + serviceName;

        Class<?> thisClass = Class.forName(hertsRpcService.getClass().getName());
        Method[] methods = thisClass.getDeclaredMethods();
        ConcurrentMap<String, List<Parameter>> methodParameters = new ConcurrentHashMap<>();
        for (Method method : methods) {
            List<Parameter> parameters = Arrays.asList(thisClass.getMethod(method.getName(), method.getParameterTypes()).getParameters());

            String endpoint = this.baseEndpoint + "/" + method.getName();
            this.methods.put(endpoint, method);
            methodParameters.put(method.getName(), parameters);
        }

        if (this.hertsHttpMetrics.isMetricsEnabled()) {
            this.hertsHttpCaller = new HertsHttpMetricsCaller(hertsRpcService, this.hertsHttpMetrics,
                    this.hertsSerializer, metricsServer, methodParameters, serviceName);
        } else {
            this.hertsHttpCaller = new HertsHttpSimpleCaller(hertsRpcService, this.hertsHttpMetrics,
                    this.hertsSerializer, methodParameters);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!this.hertsHttpMetrics.isMetricsEnabled() || !request.getRequestURI().equals("/metricsz")) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        this.hertsHttpCaller.setMetricsResponse(response);
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Allow", "GET, POST, HEAD, OPTIONS");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        this.hertsHttpCaller.setHertsHeader(response);

        Method hertsMethod = this.methods.get(request.getRequestURI());
        if (hertsMethod == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            this.hertsHttpCaller.post(hertsMethod, request, response);
        } catch (HertsInvalidBodyException | HertsJsonProcessingException ex) {
            HertsHttpErrorResponse err = genErrorResponse(HertsHttpErrorException.StatusCode.Status400, ex.getMessage());
            setError(HttpServletResponse.SC_BAD_REQUEST, response, err);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof HertsHttpErrorException) {
                HertsHttpErrorException exception = (HertsHttpErrorException) cause;
                HertsHttpErrorResponse err = genErrorResponse(exception.getStatusCode(), ex.getMessage());
                setError(exception.getStatusCode().getIntegerCode(), response, err);
            } else {
                ex.printStackTrace();
                HertsHttpErrorResponse err = genErrorResponse(HertsHttpErrorException.StatusCode.Status500, ex.getMessage());
                setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, err);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            HertsHttpErrorResponse err = genErrorResponse(HertsHttpErrorException.StatusCode.Status500, ex.getMessage());
            setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, err);
        }
    }

    @Override
    public String getBaseEndpoint() {
        return this.baseEndpoint;
    }

    @Override
    public String[] getEndpoints() {
        return this.methods.keySet().toArray(new String[0]);
    }

    public static HertsHttpErrorResponse genErrorResponse(HertsHttpErrorException.StatusCode statusCodeEnum, String message) {
        HertsHttpErrorResponse hertsResponse = new HertsHttpErrorResponse();
        hertsResponse.setStatusCode(statusCodeEnum);
        hertsResponse.setMessage(message);
        return hertsResponse;
    }

    private void setError(int statusCode, HttpServletResponse response, HertsHttpErrorResponse errorResponse) throws IOException {
        response.setStatus(statusCode);
        HertsHttpCallerBase.setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(errorResponse));
    }
}