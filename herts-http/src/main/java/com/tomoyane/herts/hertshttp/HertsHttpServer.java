package com.tomoyane.herts.hertshttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tomoyane.herts.hertscommon.context.HertsHeaderContext;
import com.tomoyane.herts.hertscommon.context.HertsHttpRequest;
import com.tomoyane.herts.hertscommon.context.HertsHttpResponse;
import com.tomoyane.herts.hertscommon.exception.HertsInstanceException;
import com.tomoyane.herts.hertscommon.exception.HertsInvalidBodyException;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializeType;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

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

public class HertsHttpServer extends HttpServlet {
    private final Object coreObject;
    private final String serviceName;
    private final HertsSerializer hertsSerializer = new HertsSerializer(HertsSerializeType.Json);
    private final ConcurrentMap<String, Method> methods = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<Parameter>> parameters = new ConcurrentHashMap<>();

    public void init() {
        System.out.println("Initialize server");
    }

    public HertsHttpServer(HertsCoreService hertsCoreService) throws ClassNotFoundException, NoSuchMethodException {
        this.serviceName = hertsCoreService.getClass().getSimpleName();

        String serviceName = hertsCoreService.getClass().getName();
        Class<?> thisClass;
        thisClass = Class.forName(serviceName);

        Method[] methods = thisClass.getDeclaredMethods();
        for (Method method : methods) {
            List<Parameter> parameters = Arrays.asList(
                    thisClass.getMethod(method.getName(), method.getParameterTypes()).getParameters());

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader(HertsHeaderContext.HERTS_CONTEXT_KEY, HertsHeaderContext.CODE_VERSION);
        response.setHeader(HertsHeaderContext.HERTS_SERVER_KEY, HertsHeaderContext.HERTS_SERVER_VAL);

        Method hertsMethod = this.methods.get(request.getRequestURI());
        if (hertsMethod == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            BufferedReader fileReader = request.getReader();
            HertsHttpRequest hertsRequest = this.hertsSerializer.deserialize(fileReader, HertsHttpRequest.class);

            var parameters = this.parameters.get(hertsMethod.getName());
            var keyNames = hertsRequest.getData().keySet();
            for (Parameter param : parameters) {
                if (!keyNames.contains(param.getName())) {
                    throw new HertsInvalidBodyException("Invalid body");
                }
            }
            var res = hertsMethod.invoke(this.coreObject, hertsRequest.getDataValues());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            if (res == null) {
                return;
            }

            var hertsResponse = new HertsHttpResponse();
            hertsResponse.setData(res);
            setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(hertsResponse));
        } catch (HertsInvalidBodyException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(Collections.singletonMap("error", ex.getMessage())));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(Collections.singletonMap("error", ex.getMessage())));
        }
    }

    private void setWriter(PrintWriter out, String msg) {
        out.print(msg);
        out.flush();
    }
}