package org.hertsstack.gateway;

import io.grpc.Status;

import org.hertsstack.core.exception.InvalidMessageException;
import org.hertsstack.core.exception.http.HttpErrorException;
import org.hertsstack.core.exception.rpc.RpcErrorException;
import org.hertsstack.core.modelx.InternalHttpMsg;
import org.hertsstack.core.modelx.InternalHttpRequest;
import org.hertsstack.core.modelx.InternalHttpResponse;
import org.hertsstack.http.InternalHttpCaller;
import org.hertsstack.metrics.HertsMetricsServer;
import org.hertsstack.serializer.MessageSerializeType;
import org.hertsstack.serializer.MessageSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

class HttpServerCaller implements InternalHttpCaller {
    private final MessageSerializer hertsSerializer = new MessageSerializer(MessageSerializeType.Json);
    private final HertsMetricsServer hertsMetricsServer;
    private final RpcClient rpcClient;

    public HttpServerCaller(HertsMetricsServer hertsMetricsServer, RpcClient rpcClient) {
        this.hertsMetricsServer = hertsMetricsServer;
        this.rpcClient = rpcClient;
    }

    @Override
    public void post(String serviceName, Method hertsMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RpcDefinition rpcDefinition = this.rpcClient.getRpcDefinition(serviceName);
        List<Parameter> parameters = rpcDefinition.getMethodParameters().get(hertsMethod.getName());
        InternalHttpRequest hertsRequest;
        if (parameters.size() > 0) {
            try {
                hertsRequest = this.hertsSerializer.deserialize(request.getReader(), InternalHttpRequest.class);
                List<String> keyNames = hertsRequest.getKeyNames();
                for (Parameter param : parameters) {
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

        try {
            Object res = this.rpcClient.callRpc(serviceName, hertsMethod.getName(), args);
            response.setStatus(HttpServletResponse.SC_OK);
            if (res == null) {
                return;
            }
            InternalHttpResponse hertsResponse = new InternalHttpResponse();
            InternalHttpMsg payload = new InternalHttpMsg();
            payload.setValue(res);
            payload.setClassInfo(hertsMethod.getReturnType().getName());
            hertsResponse.setPayload(payload);
            response.getWriter().print(this.hertsSerializer.serializeAsStr(hertsResponse));
            response.getWriter().flush();
        } catch (InvocationTargetException | IllegalAccessException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RpcErrorException) {
                RpcErrorException exception = (RpcErrorException) cause;
                throw new InvocationTargetException(HttpErrorException.from(
                        exception.getGrpcStatus(), exception.getMessage()), exception.getMessage());
            } else {
                String msg = "Unexpected error";
                throw new InvocationTargetException(HttpErrorException.from(Status.INTERNAL, msg), msg);
            }
        }
    }

    @Override
    public void setMetricsResponse(HttpServletResponse response) throws IOException {
        this.hertsMetricsServer.setMetricsResponse(response);
    }
}
