package org.hertsstack.http;

import org.hertsstack.core.exception.HttpServerBuildException;
import org.hertsstack.core.modelx.InternalHttpErrorResponse;
import org.hertsstack.core.exception.InvalidMessageException;
import org.hertsstack.core.exception.http.HttpErrorException;
import org.hertsstack.core.logger.Logging;
import org.hertsstack.core.service.HertsService;
import org.hertsstack.serializer.MessageSerializeType;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.serializer.MessageJsonParsingException;
import org.hertsstack.metrics.HertsMetrics;
import org.hertsstack.metrics.HertsMetricsServer;

import javax.annotation.Nullable;
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
 * @version 1.0.2
 */
public class InternalHttpServlet extends HttpServlet {
    private static final java.util.logging.Logger logger = Logging.getLogger(InternalHttpServlet.class.getSimpleName());
    private static final MessageSerializer hertsSerializer = new MessageSerializer(MessageSerializeType.Json);

    // HertsMetrics instance
    private final HertsMetrics hertsHttpMetrics;

    // Key is endpoint
    private final ConcurrentMap<String, Method> methodMap;

    // Key is HertsService string name
    private final ConcurrentMap<String, InternalHttpCaller> callerMap;

    // Base endpoint list
    private final List<String> baseEndpoints;

    /**
     * Gateway Server.
     * Create InternalHttpServlet instance by HertsService interface Class list.
     *
     * @param hertsRpcServices Class of HertsService interface
     * @param hertsHttpMetrics HertsMetrics
     * @param caller HertsHttpCaller
     * @return HertsHttpServerCore
     */
    public static InternalHttpServlet createGateway(List<Class<?>> hertsRpcServices,
                                                    HertsMetrics hertsHttpMetrics,
                                                    InternalHttpCaller caller) {
        List<String> baseEndpoints = new ArrayList<>();
        ConcurrentMap<String, InternalHttpCaller> callerMap = new ConcurrentHashMap<>();
        ConcurrentMap<String, Method> methodMap = new ConcurrentHashMap<>();

        for (Class<?> hertsServiceClass : hertsRpcServices) {
            if (!hertsServiceClass.isInterface()) {
                throw new HttpServerBuildException("Herts service is not interface");
            }

            String serviceName = hertsServiceClass.getSimpleName();
            String baseEndpoint = "/gateway/" + serviceName;
            baseEndpoints.add(baseEndpoint);

            try {
                Method[] defMethods = hertsServiceClass.getDeclaredMethods();
                for (Method method : defMethods) {
                    String endpoint = baseEndpoint + "/" + method.getName();

                    methodMap.put(endpoint, method);
                }

                callerMap.put(serviceName, caller);
            } catch (Exception ex) {
                throw new HttpServerBuildException("Failed to build http server", ex);
            }
        }
        return new InternalHttpServlet(hertsHttpMetrics, baseEndpoints, methodMap, callerMap);

    }

    /**
     * API Server.
     * Create InternalHttpServlet instance by HertsService list.
     *
     * @param hertsRpcServices HertsService list
     * @param hertsHttpMetrics HertsMetrics
     * @param metricsServer HertsMetricsServer
     * @param caller HertsHttpCaller
     * @return HertsHttpServerCore
     */
    public static InternalHttpServlet createByHertsService(List<HertsService> hertsRpcServices,
                                                           HertsMetrics hertsHttpMetrics,
                                                           HertsMetricsServer metricsServer,
                                                           @Nullable InternalHttpCaller caller)  {

        List<String> baseEndpoints = new ArrayList<>();
        ConcurrentMap<String, InternalHttpCaller> callerMap = new ConcurrentHashMap<>();
        ConcurrentMap<String, Method> methodMap = new ConcurrentHashMap<>();

        for (HertsService hertsService : hertsRpcServices) {
            String serviceName = hertsService.getClass().getInterfaces()[0].getSimpleName();
            String baseEndpoint = "/api/" + serviceName;
            baseEndpoints.add(baseEndpoint);

            try {
                Class<?> thisClass = Class.forName(hertsService.getClass().getName());
                Method[] defMethods = thisClass.getDeclaredMethods();
                ConcurrentMap<String, List<Parameter>> methodParameters = new ConcurrentHashMap<>();
                for (Method method : defMethods) {
                    List<Parameter> parameters = Arrays.asList(thisClass.getMethod(method.getName(), method.getParameterTypes()).getParameters());
                    String endpoint = baseEndpoint + "/" + method.getName();

                    methodMap.put(endpoint, method);
                    methodParameters.put(method.getName(), parameters);
                }

                if (caller != null) {
                    callerMap.put(serviceName, caller);
                } else if (hertsHttpMetrics.isMetricsEnabled()) {
                    callerMap.put(serviceName, new HertsHttpMetricsCaller(hertsService, hertsHttpMetrics,
                            hertsSerializer, metricsServer, methodParameters, serviceName));
                } else {
                    callerMap.put(serviceName, new HertsHttpSimpleCaller(hertsService, hertsHttpMetrics,
                            hertsSerializer, methodParameters));
                }
            } catch (Exception ex) {
                throw new HttpServerBuildException("Failed to build http server", ex);
            }
        }
        return new InternalHttpServlet(hertsHttpMetrics, baseEndpoints, methodMap, callerMap);
    }

    /**
     * Private constructor.
     * Please see factory method.
     *
     * @param hertsHttpMetrics HertsMetrics
     * @param baseEndpoints Server base endpoint list
     * @param methodMap Method info
     * @param callerMap Caller info
     */
    private InternalHttpServlet(HertsMetrics hertsHttpMetrics, List<String> baseEndpoints,
                                ConcurrentMap<String, Method> methodMap, ConcurrentMap<String, InternalHttpCaller> callerMap) {

        this.hertsHttpMetrics = hertsHttpMetrics;
        this.hertsHttpMetrics.register();
        this.baseEndpoints = baseEndpoints;
        this.methodMap = methodMap;
        this.callerMap = callerMap;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        if (!isMetricsEndpoint(uri)) {
            InternalHttpErrorResponse err = genErrorResponse(HttpErrorException.StatusCode.Status404, "Not found");
            setError(HttpServletResponse.SC_NOT_FOUND, response, err);
            return;
        }
        this.callerMap.entrySet().iterator().next().getValue().setMetricsResponse(response);
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        if (isMetricsEndpoint(request.getRequestURI())) {
            response.setHeader("Allow", "GET, HEAD, OPTIONS");
            return;
        }

        ParedHttpRequestInfo requestInfo = parsedRequestInfo(request.getRequestURI());
        if (requestInfo != null){
            response.setHeader("Allow", "POST, HEAD, OPTIONS");
            return;
        }

        InternalHttpErrorResponse err = genErrorResponse(HttpErrorException.StatusCode.Status404, "Not found");
        setError(HttpServletResponse.SC_NOT_FOUND, response, err);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        ParedHttpRequestInfo requestInfo = parsedRequestInfo(uri);
        if (requestInfo == null) {
            InternalHttpErrorResponse err = genErrorResponse(HttpErrorException.StatusCode.Status404, "Not found");
            setError(HttpServletResponse.SC_NOT_FOUND, response, err);
            return;
        }

        try {
            this.callerMap.get(requestInfo.getServiceName()).post(requestInfo.getServiceName(), requestInfo.getHertsMethod(), request, response);

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

    public String[] getEndpoints() {
        return this.methodMap.keySet().toArray(new String[0]);
    }

    private ParedHttpRequestInfo parsedRequestInfo(String uri) {
        String serviceName = extractServiceName(uri);
        if (serviceName == null) {
            return null;
        }

        Method hertsMethod = this.methodMap.get(uri);
        if (hertsMethod == null) {
            return null;
        }
        return new ParedHttpRequestInfo(serviceName, hertsMethod);
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
        return this.hertsHttpMetrics.isMetricsEnabled() && url.equals("/metricsz");
    }

    public static InternalHttpErrorResponse genErrorResponse(HttpErrorException.StatusCode statusCodeEnum, String message) {
        InternalHttpErrorResponse hertsResponse = new InternalHttpErrorResponse();
        hertsResponse.setStatusCodeEnum(statusCodeEnum);
        hertsResponse.setMessage(message);
        return hertsResponse;
    }

    private void setError(int statusCode, HttpServletResponse response, InternalHttpErrorResponse errorResponse) throws IOException {
        response.setStatus(statusCode);
        HertsHttpCallerBase.setWriter(response.getWriter(), hertsSerializer.serializeAsStr(errorResponse));
    }
}