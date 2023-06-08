package org.herts.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.herts.core.serializer.HertsSerializer;
import org.herts.metrics.HertsMetricsServer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CallerConstructor {
    private Class<?> c;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private HertsMetricsServer metricsServer;
    private HertsSerializer serializer;

    public CallerConstructor(Class<?> c, HttpServletRequest req, HttpServletResponse res,
                             HertsMetricsServer metricsServer, HertsSerializer serializer) {
        this.c = c;
        this.req = req;
        this.res = res;
        this.metricsServer = metricsServer;
        this.serializer = serializer;
    }

    public ConcurrentMap<String, List<Parameter>> getParameters() {
        ConcurrentMap<String, List<Parameter>> map = new ConcurrentHashMap<>();
        Method[] methods = getMethods();
        for (Method m : methods) {
            map.put(m.getName(), Arrays.asList(m.getParameters()));
        }
        return map;
    }

    public Method[] getMethods() {
        return c.getDeclaredMethods();
    }

    public Object createCInstance() throws InstantiationException, IllegalAccessException {
        return c.newInstance();
    }

    public Class<?> getC() {
        return c;
    }

    public HttpServletRequest getReq() {
        return req;
    }

    public HttpServletResponse getRes() {
        return res;
    }

    public HertsMetricsServer getMetricsServer() {
        return metricsServer;
    }

    public HertsSerializer getSerializer() {
        return serializer;
    }
}
