package org.herts.http;

import org.herts.common.context.HertsSystemContext;
import org.herts.common.context.HertsHttpRequest;
import org.herts.common.context.HertsHttpResponse;
import org.herts.common.context.Payload;
import org.herts.common.exception.HertsInvalidBodyException;
import org.herts.common.serializer.HertsSerializer;
import org.herts.metrics.server.HertsMetricsServer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
public class HertsHttpCallerBase {
    private final Object coreObject;
    private final HertsMetricsServer hertsMetricsServer;
    private final HertsSerializer hertsSerializer;
    private final ConcurrentMap<String, List<Parameter>> parameters;

    public HertsHttpCallerBase(Object coreObject, HertsMetricsServer hertsMetricsServer,
                               HertsSerializer hertsSerializer, ConcurrentMap<String, List<Parameter>> parameters) {

        this.coreObject = coreObject;
        this.hertsMetricsServer = hertsMetricsServer;
        this.hertsSerializer = hertsSerializer;
        this.parameters = parameters;
    }

    public void setWriter(PrintWriter out, String msg) {
        out.print(msg);
        out.flush();
    }

    public void setMetricsResponse(HttpServletResponse response) throws IOException {
        this.hertsMetricsServer.setMetricsResponse(response);
    }

    public void setHertsHeader(HttpServletResponse response) {
        response.setHeader(HertsSystemContext.Header.HERTS_CONTEXT_KEY, HertsSystemContext.Header.CODE_VERSION);
        response.setHeader(HertsSystemContext.Header.HERTS_SERVER_KEY, HertsSystemContext.Header.HERTS_SERVER_VAL);
    }

    protected void call(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        var parameters = this.parameters.get(hertsMethod.getName());
        HertsHttpRequest hertsRequest;

        if (parameters.size() > 0) {
            hertsRequest = this.hertsSerializer.deserialize(request.getReader(), HertsHttpRequest.class);

            var keyNames = hertsRequest.getKeyNames();
            for (Parameter param : parameters) {
                if (!keyNames.contains(param.getName())) {
                    throw new HertsInvalidBodyException("Invalid body");
                }
            }
        } else {
            hertsRequest = new HertsHttpRequest();
            hertsRequest.setPayloads(new ArrayList<>());
        }

        List<Payload> payloads = hertsRequest.getPayloads();
        Object[] args = new Object[payloads.size()];
        int idx = 0;
        for (Payload payload : payloads) {
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

        var hertsResponse = new HertsHttpResponse();
        var payload = new Payload();
        payload.setValue(res);
        payload.setClassInfo(hertsMethod.getReturnType().getName());
        hertsResponse.setPayload(payload);
        hertsResponse.setExceptionCauseMessage(null);
        setWriter(response.getWriter(), this.hertsSerializer.serializeAsStr(hertsResponse));
    }
}
