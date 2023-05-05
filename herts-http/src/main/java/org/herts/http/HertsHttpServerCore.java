package org.herts.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.herts.common.context.HertsHttpResponse;
import org.herts.common.exception.HertsInstanceException;
import org.herts.common.exception.HertsInvalidBodyException;
import org.herts.common.logger.HertsLogger;
import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.common.service.HertsService;
import org.herts.metrics.HertsMetrics;
import org.herts.metrics.server.HertsMetricsServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Herts http server core
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpServerCore extends HttpServlet implements HertsHttpServer {
    private static final Logger logger = HertsLogger.getLogger(HertsHttpServerCore.class.getSimpleName());

    private final HertsMetrics hertsHttpMetrics;
    private final HertsHttpCaller hertsHttpCaller;
    private final HertsSerializer hertsSerializer = new HertsSerializer(HertsSerializeType.Json);
    private final ConcurrentMap<String, Method> methods = new ConcurrentHashMap<>();
    private final String baseEndpoint;

    public void init() {
    }

    public HertsHttpServerCore(HertsService hertsRpcService, HertsMetrics hertsHttpMetrics, HertsMetricsServer metricsServer)
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

        Object coreObject;
        try {
            coreObject = thisClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HertsInstanceException(e);
        }

        if (this.hertsHttpMetrics.isMetricsEnabled()) {
            this.hertsHttpCaller = new HertsHttpMetricsCaller(coreObject, this.hertsHttpMetrics,
                    this.hertsSerializer, metricsServer, methodParameters, serviceName);
        } else {
            this.hertsHttpCaller = new HertsHttpSimpleCaller(coreObject, this.hertsHttpMetrics,
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
        } catch (HertsInvalidBodyException | JsonProcessingException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            var hertsResponse = new HertsHttpResponse();
            hertsResponse.setExceptionCauseMessage(ex.getMessage());
            this.hertsHttpCaller.setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(hertsResponse));
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            var hertsResponse = new HertsHttpResponse();
            hertsResponse.setExceptionCauseMessage(ex.getMessage());
            this.hertsHttpCaller.setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(hertsResponse));
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
}