package org.hertsstack.httpclient;

import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.InvalidMessageException;
import org.hertsstack.core.modelx.InternalHttpErrorResponse;
import org.hertsstack.core.modelx.InternalHttpRequest;
import org.hertsstack.core.modelx.InternalHttpResponse;
import org.hertsstack.core.modelx.InternalHttpMsg;
import org.hertsstack.core.exception.ServiceNotFoundException;
import org.hertsstack.core.modelx.RegisteredMethod;
import org.hertsstack.serializer.MessageSerializeType;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.core.service.HertsService;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

/**
 * Herts http client handler
 *
 * @author Herts Contributer
 */
public class HertsHttpClientHandler implements InvocationHandler {
    private final MessageSerializer serializer = new MessageSerializer(MessageSerializeType.Json);
    private final ConcurrentMap<String, RegisteredMethod> registeredMethods = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url;
    private final String serviceName;
    private final String endpointPref;

    private ConcurrentMap<String, String> customHeaders;

    public HertsHttpClientHandler(String url, Class<?> hertsRpcService, boolean isGateway) {
        this.url = url;
        this.serviceName = hertsRpcService.getSimpleName();
        this.endpointPref = !isGateway ? "api" : "gateway";
        init(hertsRpcService.getName());
    }

    public HertsHttpClientHandler(String url, Class<?> hertsRpcService, Map<String, String> customHeaders, boolean isGateway) {
        this.url = url;
        this.serviceName = hertsRpcService.getSimpleName();
        this.endpointPref = !isGateway ? "api" : "gateway";
        this.customHeaders = new ConcurrentHashMap<>();
        this.customHeaders.putAll(customHeaders);
        init(hertsRpcService.getName());
    }

    private void init(String instanceName) {
        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(instanceName);
        } catch (ClassNotFoundException ignore) {
            throw new ServiceNotFoundException("Unknown class name. Allowed class is " + HertsService.class.getName());
        }

        Method[] methods = hertsServiceClass.getDeclaredMethods();
        for (Method method : methods) {
            RegisteredMethod registeredMethod = new RegisteredMethod(
                    HertsType.Http,
                    instanceName,
                    instanceName,
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes()
            );
            this.registeredMethods.put(method.getName(), registeredMethod);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RegisteredMethod registeredMethod = this.registeredMethods.get(method.getName());
        if (registeredMethod == null) {
            throw new InvalidMessageException("Invalid herts method.");
        }

        HttpRequest.Builder builder = HttpRequest
                .newBuilder(URI.create(url + "/" + endpointPref + "/" + this.serviceName + "/" + method.getName()))
                .header("Content-Type", "application/json");

        if (this.customHeaders != null) {
            for (Map.Entry<String, String> entry : this.customHeaders.entrySet()) {
                builder = builder.header(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest httpRequest;
        InternalHttpRequest body = new InternalHttpRequest();
        if (args != null) {
            Class<?>[] parameterTypes = registeredMethod.getParameterClasses();
            if (parameterTypes == null || args.length != parameterTypes.length) {
                throw new InvalidMessageException("Invalid herts method.");
            }

            List<InternalHttpMsg> payloads = new ArrayList<>();
            IntStream.range(0, args.length).forEach(idx -> {
                Object requestArg = args[idx];
                InternalHttpMsg payload = new InternalHttpMsg();
                payload.setKeyName("arg" + idx);
                payload.setValue(requestArg);
                payloads.add(payload);
            });
            body.setPayloads(payloads);
            httpRequest = builder
                    .POST(HttpRequest.BodyPublishers.ofString(this.serializer.serializeAsStr(body)))
                    .build();
        } else {
            body.setPayloads(new ArrayList<>());
            httpRequest = builder
                    .POST(HttpRequest.BodyPublishers.ofString(this.serializer.serializeAsStr(body)))
                    .build();
        }

        HttpResponse<byte[]> httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() > 299) {
            throwHertsHttpError(httpResponse);
            return null;
        }

        InternalHttpResponse deserialize = this.serializer.deserialize(httpResponse.body(), InternalHttpResponse.class);
        if (deserialize == null) {
            return null;
        }

        InternalHttpMsg payload = deserialize.getPayload();
        return this.serializer.convertFromHertHttpPayload(payload.getValue(), registeredMethod.getMethodReturnType());
    }

    private void throwHertsHttpError(HttpResponse<byte[]> httpResponse) throws IOException {
        InternalHttpErrorResponse deserialize = this.serializer.deserialize(httpResponse.body(), InternalHttpErrorResponse.class);
        deserialize.throwHertsHttpErrorException();
    }
}
