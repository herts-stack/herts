package org.herts.http;

import org.herts.serializer.MessageJsonParsingException;
import org.herts.core.modelx.InternalHttpErrorResponse;
import org.herts.core.exception.InvalidMessageException;
import org.herts.core.exception.http.HttpErrorException;
import org.herts.core.logger.Logging;
import org.herts.serializer.MessageSerializeType;
import org.herts.serializer.MessageSerializer;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http server core
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class HertsHttpServerCoreImpl extends HttpServlet implements HertsHttpServerCore {
    private static final java.util.logging.Logger logger = Logging.getLogger(HertsHttpServerCore.class.getSimpleName());

    private final HertsMetrics hertsHttpMetrics;
    private final MessageSerializer hertsSerializer = new MessageSerializer(MessageSerializeType.Json);

    // Key is endpoint
    private final ConcurrentMap<String, Method> methods = new ConcurrentHashMap<>();

    // Key is HertsService string name
    private final ConcurrentMap<String, HertsHttpCaller> callers = new ConcurrentHashMap<>();

    private final List<String> baseEndpoints = new ArrayList<>();

    public void init() {
    }

    public HertsHttpServerCoreImpl(List<HertsService> hertsRpcServices, HertsMetrics hertsHttpMetrics, HertsMetricsServer metricsServer)
            throws ClassNotFoundException, NoSuchMethodException {

        this.hertsHttpMetrics = hertsHttpMetrics;
        this.hertsHttpMetrics.register();

        for (HertsService hertsRpcService : hertsRpcServices) {
            String serviceName = hertsRpcService.getClass().getInterfaces()[0].getSimpleName();
            String baseEndpoint = "/api/" + serviceName;
            this.baseEndpoints.add(baseEndpoint);

            Class<?> thisClass = Class.forName(hertsRpcService.getClass().getName());
            Method[] methods = thisClass.getDeclaredMethods();
            ConcurrentMap<String, List<Parameter>> methodParameters = new ConcurrentHashMap<>();
            for (Method method : methods) {
                List<Parameter> parameters = Arrays.asList(thisClass.getMethod(method.getName(), method.getParameterTypes()).getParameters());
                String endpoint = baseEndpoint + "/" + method.getName();
                this.methods.put(endpoint, method);
                methodParameters.put(method.getName(), parameters);
            }

            if (this.hertsHttpMetrics.isMetricsEnabled()) {
                this.callers.put(serviceName, new HertsHttpMetricsCaller(hertsRpcService, this.hertsHttpMetrics,
                        this.hertsSerializer, metricsServer, methodParameters, serviceName));
            } else {
                this.callers.put(serviceName, new HertsHttpSimpleCaller(hertsRpcService, this.hertsHttpMetrics,
                        this.hertsSerializer, methodParameters));
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        if (!isMetricsEndpoint(uri)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        this.callers.entrySet().iterator().next().getValue().setMetricsResponse(response);
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) {
        if (isMetricsEndpoint(request.getRequestURI())) {
            response.setHeader("Allow", "POST, HEAD, OPTIONS");
        } else {
            response.setHeader("Allow", "GET, HEAD, OPTIONS");
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String uri = request.getRequestURI();
        String serviceName = extractServiceName(uri);
        if (serviceName == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        this.callers.get(serviceName).setHertsHeader(response);

        Method hertsMethod = this.methods.get(request.getRequestURI());
        if (hertsMethod == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            this.callers.get(serviceName).post(hertsMethod, request, response);
        } catch (InvalidMessageException | MessageJsonParsingException ex) {
            InternalHttpErrorResponse err = genErrorResponse(HttpErrorException.StatusCode.Status400, ex.getMessage());
            setError(HttpServletResponse.SC_BAD_REQUEST, response, err);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof HttpErrorException) {
                HttpErrorException exception = (HttpErrorException) cause;
                InternalHttpErrorResponse err = genErrorResponse(exception.getStatusCode(), ex.getMessage());
                setError(exception.getStatusCode().getIntegerCode(), response, err);
            } else {
                ex.printStackTrace();
                InternalHttpErrorResponse err = genErrorResponse(HttpErrorException.StatusCode.Status500, ex.getMessage());
                setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, err);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            InternalHttpErrorResponse err = genErrorResponse(HttpErrorException.StatusCode.Status500, ex.getMessage());
            setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response, err);
        }
    }

    @Override
    public String[] getEndpoints() {
        return this.methods.keySet().toArray(new String[0]);
    }

    private String extractServiceName(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        String[] splitUrls = url.split("/");
        if (splitUrls.length < 3) {
            return null;
        }
        return splitUrls[2];
    }

    private boolean isMetricsEndpoint(String url) {
        if (this.hertsHttpMetrics.isMetricsEnabled() && url.equals("/metricsz")) {
            return true;
        }
        return false;
    }

    public static InternalHttpErrorResponse genErrorResponse(HttpErrorException.StatusCode statusCodeEnum, String message) {
        InternalHttpErrorResponse hertsResponse = new InternalHttpErrorResponse();
        hertsResponse.setStatusCode(statusCodeEnum);
        hertsResponse.setMessage(message);
        return hertsResponse;
    }

    private void setError(int statusCode, HttpServletResponse response, InternalHttpErrorResponse errorResponse) throws IOException {
        response.setStatus(statusCode);
        HertsHttpCallerBase.setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(errorResponse));
    }
}