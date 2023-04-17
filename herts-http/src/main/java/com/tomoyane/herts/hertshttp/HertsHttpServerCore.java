package com.tomoyane.herts.hertshttp;

import com.tomoyane.herts.hertscommon.context.HertsHeaderContext;
import com.tomoyane.herts.hertscommon.context.HertsHttpRequest;
import com.tomoyane.herts.hertscommon.context.HertsHttpResponse;
import com.tomoyane.herts.hertscommon.exception.HertsInstanceException;
import com.tomoyane.herts.hertscommon.exception.HertsInvalidBodyException;
import com.tomoyane.herts.hertscommon.logger.HertsLogger;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializeType;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.hertsmetrics.HertsCoreMetrics;
import com.tomoyane.herts.hertsmetrics.context.HertsMetricsContext;
import com.tomoyane.herts.hertsmetrics.context.HertsTimer;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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

    private final Object coreObject;
    private final String serviceName;
    private final HertsCoreMetrics hertsHttpMetrics;
    private final HertsSerializer hertsSerializer = new HertsSerializer(HertsSerializeType.Json);
    private final ConcurrentMap<String, Method> methods = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<Parameter>> parameters = new ConcurrentHashMap<>();

    public void init() {
    }

    public HertsHttpServerCore(HertsCoreService hertsCoreService, HertsCoreMetrics hertsHttpMetrics) throws ClassNotFoundException, NoSuchMethodException {
        this.serviceName = hertsCoreService.getClass().getInterfaces()[0].getSimpleName();
        this.hertsHttpMetrics = hertsHttpMetrics;
        this.hertsHttpMetrics.register();

        Class<?> thisClass = Class.forName(hertsCoreService.getClass().getName());
        Method[] methods = thisClass.getDeclaredMethods();
        for (Method method : methods) {
            List<Parameter> parameters = Arrays.asList(thisClass.getMethod(method.getName(), method.getParameterTypes()).getParameters());

            String endpoint = "/api/" + this.serviceName + "/" + method.getName();
            this.methods.put(endpoint, method);
            this.parameters.put(method.getName(), parameters);
        }

        try {
            this.coreObject = thisClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HertsInstanceException(e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!this.hertsHttpMetrics.isMetricsEnabled() || !request.getRequestURI().equals("/metricsz")) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        setHertsHeader(response);
        setMetrics(response);
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
        setHertsHeader(response);

        Method hertsMethod = this.methods.get(request.getRequestURI());
        if (hertsMethod == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (this.hertsHttpMetrics.isMetricsEnabled()) {
            HertsTimer timer = null;
            try {
                if (this.hertsHttpMetrics.isLatencyEnabled()) {
                    timer = this.hertsHttpMetrics.startLatencyTimer(hertsMethod.getName());
                    this.hertsHttpMetrics.stopLatencyTimer(timer);
                }
                if (this.hertsHttpMetrics.isRpsEnabled()) {
                    this.hertsHttpMetrics.counter(HertsMetricsContext.Metric.Rps, hertsMethod.getName());
                }
                call(hertsMethod, request, response);
                if (this.hertsHttpMetrics.isLatencyEnabled()) {
                    this.hertsHttpMetrics.stopLatencyTimer(timer);
                }
            } catch (HertsInvalidBodyException ex) {
                if (this.hertsHttpMetrics.isErrRateEnabled()) {
                    this.hertsHttpMetrics.counter(HertsMetricsContext.Metric.ErrRate, hertsMethod.getName());
                }
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(Collections.singletonMap("error", ex.getMessage())));
            } catch (Exception ex) {
                if (this.hertsHttpMetrics.isErrRateEnabled()) {
                    this.hertsHttpMetrics.counter(HertsMetricsContext.Metric.ErrRate, hertsMethod.getName());
                }
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(Collections.singletonMap("error", ex.getMessage())));
            }
        } else {
            try {
                call(hertsMethod, request, response);
            } catch (HertsInvalidBodyException ex) {
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(Collections.singletonMap("error", ex.getMessage())));
            } catch (Exception ex) {
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(Collections.singletonMap("error", ex.getMessage())));
            }
        }
    }

    @Override
    public String[] getEndpoints() {
        return this.methods.keySet().toArray(new String[0]);
    }

    private void call(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        var parameters = this.parameters.get(hertsMethod.getName());
        HertsHttpRequest hertsRequest = null;
        if (parameters.size() > 0) {
            BufferedReader fileReader = request.getReader();
            hertsRequest = this.hertsSerializer.deserialize(fileReader, HertsHttpRequest.class);

            var keyNames = hertsRequest.getData().keySet();
            for (Parameter param : parameters) {
                if (!keyNames.contains(param.getName())) {
                    throw new HertsInvalidBodyException("Invalid body");
                }
            }
        } else {
            hertsRequest = new HertsHttpRequest();
            hertsRequest.setData(Collections.emptyMap());
        }

        var res = hertsMethod.invoke(this.coreObject, hertsRequest.getDataValues());
        response.setStatus(HttpServletResponse.SC_OK);
        if (res == null) {
            return;
        }

        var hertsResponse = new HertsHttpResponse();
        hertsResponse.setData(res);
        setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(hertsResponse));
    }

    private void setHertsHeader(HttpServletResponse response) {
        response.setHeader(HertsHeaderContext.HERTS_CONTEXT_KEY, HertsHeaderContext.CODE_VERSION);
        response.setHeader(HertsHeaderContext.HERTS_SERVER_KEY, HertsHeaderContext.HERTS_SERVER_VAL);
    }

    private void setWriter(PrintWriter out, String msg) {
        out.print(msg);
        out.flush();
    }

    private void setMetrics(HttpServletResponse response) throws IOException {
        response.setContentType(this.hertsHttpMetrics.getPrometheusFormat());
        response.setStatus(HttpServletResponse.SC_OK);
        var w = response.getWriter();
        w.print(this.hertsHttpMetrics.scrape());
        w.flush();
    }
}