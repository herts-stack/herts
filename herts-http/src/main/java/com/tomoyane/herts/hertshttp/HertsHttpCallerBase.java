package com.tomoyane.herts.hertshttp;

import com.tomoyane.herts.hertscommon.context.HertsHeaderContext;
import com.tomoyane.herts.hertscommon.context.HertsHttpRequest;
import com.tomoyane.herts.hertscommon.context.HertsHttpResponse;
import com.tomoyane.herts.hertscommon.exception.HertsInvalidBodyException;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import com.tomoyane.herts.hertsmetrics.HertsMetrics;
import com.tomoyane.herts.hertsmetrics.server.HertsMetricsServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
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
        response.setHeader(HertsHeaderContext.HERTS_CONTEXT_KEY, HertsHeaderContext.CODE_VERSION);
        response.setHeader(HertsHeaderContext.HERTS_SERVER_KEY, HertsHeaderContext.HERTS_SERVER_VAL);
    }

    protected void call(Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        var parameters = this.parameters.get(hertsMethod.getName());
        HertsHttpRequest hertsRequest;
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
}
