package org.herts.http;

import org.herts.core.context.SharedServiceContext;
import org.herts.core.modelx.InternalHttpRequest;
import org.herts.core.modelx.InternalHttpResponse;
import org.herts.core.modelx.InternalHttpMsg;
import org.herts.core.exception.InvalidMessageException;
import org.herts.core.serializer.MessageSerializer;
import org.herts.metrics.HertsMetricsServer;

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
 * @version 1.0.0
 */
class HertsHttpCallerBase {
    private final Object coreObject;
    private final HertsMetricsServer hertsMetricsServer;
    private final MessageSerializer hertsSerializer;
    private final ConcurrentMap<String, List<Parameter>> parameters;

    public HertsHttpCallerBase(Object coreObject, HertsMetricsServer hertsMetricsServer,
                               MessageSerializer hertsSerializer, ConcurrentMap<String, List<Parameter>> parameters) {

        this.coreObject = coreObject;
        this.hertsMetricsServer = hertsMetricsServer;
        this.hertsSerializer = hertsSerializer;
        this.parameters = parameters;
    }

    public static void setWriter(PrintWriter out, String msg) {
        out.print(msg);
        out.flush();
    }

    public void setMetricsResponse(HttpServletResponse response) throws IOException {
        this.hertsMetricsServer.setMetricsResponse(response);
    }

    public void setHertsHeader(HttpServletResponse response) {
        response.setHeader(SharedServiceContext.Header.HERTS_CONTEXT_VERSION, SharedServiceContext.Header.CODE_VERSION);
        response.setHeader(SharedServiceContext.Header.HERTS_SERVER_KEY, SharedServiceContext.Header.HERTS_SERVER_VAL);
    }

    protected void call(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Parameter> parameters = this.parameters.get(hertsMethod.getName());
        InternalHttpRequest hertsRequest;

        if (parameters.size() > 0) {
            hertsRequest = this.hertsSerializer.deserialize(request.getReader(), InternalHttpRequest.class);

            List<String> keyNames = hertsRequest.getKeyNames();
            for (Parameter param : parameters) {
                if (!keyNames.contains(param.getName())) {
                    throw new InvalidMessageException("Invalid body");
                }
            }
        } else {
            hertsRequest = new InternalHttpRequest();
            hertsRequest.setPayloads(new ArrayList<>());
        }

        List<InternalHttpMsg> payloads = hertsRequest.getPayloads();
        Object[] args = new Object[payloads.size()];
        int idx = 0;
        for (InternalHttpMsg payload : payloads) {
            Object castedArg;
            try {
                Class<?> aClass = Class.forName(payload.getClassInfo());
                castedArg = this.hertsSerializer.convertFromHertHttpPayload(payload.getValue(), aClass);
            } catch (ClassNotFoundException ex) {
                castedArg = payload.getValue();
            }
            args[idx] = castedArg;
            idx++;
        }

        Object res = hertsMethod.invoke(this.coreObject, args);
        response.setStatus(HttpServletResponse.SC_OK);
        if (res == null) {
            return;
        }

        InternalHttpResponse hertsResponse = new InternalHttpResponse();
        InternalHttpMsg payload = new InternalHttpMsg();
        payload.setValue(res);
        payload.setClassInfo(hertsMethod.getReturnType().getName());
        hertsResponse.setPayload(payload);
        hertsResponse.setExceptionCauseMessage(null);
        setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(hertsResponse));
    }
}
