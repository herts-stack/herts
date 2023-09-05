package org.hertsstack.http;

import org.hertsstack.core.context.SharedServiceContext;
import org.hertsstack.core.modelx.InternalHttpRequest;
import org.hertsstack.core.modelx.InternalHttpResponse;
import org.hertsstack.core.modelx.InternalHttpMsg;
import org.hertsstack.core.exception.InvalidMessageException;
import org.hertsstack.core.modelx.RegisteredMethod;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.metrics.HertsMetricsServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http caller base class
 *
 * @author Herts Contributer
 */
class HertsHttpCallerBase {
    private final Object coreObject;
    private final HertsMetricsServer hertsMetricsServer;
    private final MessageSerializer hertsSerializer;
    private final ConcurrentMap<String, RegisteredMethod> registeredMethods;

    public HertsHttpCallerBase(Object coreObject, HertsMetricsServer hertsMetricsServer,
                               MessageSerializer hertsSerializer, ConcurrentMap<String, RegisteredMethod> methods) {

        this.coreObject = coreObject;
        this.hertsMetricsServer = hertsMetricsServer;
        this.hertsSerializer = hertsSerializer;
        this.registeredMethods = methods;
    }

    public static void setWriter(PrintWriter out, String msg) {
        out.print(msg);
        out.flush();
    }

    public static void setHertsHeader(HttpServletResponse response, boolean isApiServer) {
        if (isApiServer) {
            response.setHeader(SharedServiceContext.Header.HERTS_SERVER_KEY, SharedServiceContext.Header.HERTS_SERVER_VAL);
        } else {
            response.setHeader(SharedServiceContext.Header.HERTS_SERVER_KEY, SharedServiceContext.Header.HERTS_SERVER_GATEWAY_VAL);
        }
        response.setHeader(SharedServiceContext.Header.HERTS_CONTEXT_VERSION, SharedServiceContext.Header.CODE_VERSION);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    public void setMetricsResponse(HttpServletResponse response) throws IOException {
        this.hertsMetricsServer.setMetricsResponse(response);
    }

    protected void call(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RegisteredMethod registeredMethod = this.registeredMethods.get(hertsMethod.getName());
        InternalHttpRequest hertsRequest;

        if (registeredMethod == null) {
            throw new InvalidMessageException("Invalid method name.");
        }

        if (registeredMethod.getParameters().length > 0) {
            try {
                hertsRequest = this.hertsSerializer.deserialize(request.getReader(), InternalHttpRequest.class);
                List<String> keyNames = hertsRequest.getKeyNames();
                for (Parameter param : registeredMethod.getParameters()) {
                    if (!keyNames.contains(param.getName())) {
                        throw new InvalidMessageException("Invalid request body");
                    }
                }
            } catch (IOException | java.lang.NullPointerException ex) {
                throw new InvalidMessageException("Invalid request body");
            }
        } else {
            hertsRequest = new InternalHttpRequest();
            hertsRequest.setPayloads(new ArrayList<>());
        }

        List<InternalHttpMsg> payloads = hertsRequest.getPayloads();
        System.out.println(hertsSerializer.serializeAsStr(payloads));
        Object[] args = new Object[payloads.size()];
        int idx = 0;
        for (InternalHttpMsg payload : payloads) {
            Object castedArg;
            try {
                Class<?> aClass = registeredMethod.getParameterClasses()[idx];
                castedArg = this.hertsSerializer.convertFromHertHttpPayload(payload.getValue(), aClass);
            } catch (Exception ex) {
                castedArg = payload.getValue();
            }
            args[idx] = castedArg;
            idx++;
        }

        System.out.println(hertsSerializer.serializeAsStr(args));
        Object res = hertsMethod.invoke(this.coreObject, args);
        response.setStatus(HttpServletResponse.SC_OK);
        if (res == null) {
            return;
        }

        InternalHttpResponse hertsResponse = new InternalHttpResponse();
        InternalHttpMsg payload = new InternalHttpMsg();
        payload.setKeyName("response");
        payload.setValue(res);
        hertsResponse.setPayload(payload);
        setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(hertsResponse));
    }
}
