package org.hertsstack.httpclient;

import org.hertsstack.core.exception.InvalidMessageException;
import org.hertsstack.core.modelx.InternalHttpErrorResponse;
import org.hertsstack.core.modelx.InternalHttpRequest;
import org.hertsstack.core.modelx.InternalHttpResponse;
import org.hertsstack.core.modelx.InternalHttpMsg;
import org.hertsstack.core.exception.ServiceNotFoundException;
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
import java.util.Arrays;
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
    private final ConcurrentMap<String, List<Class<?>>> methodParameters = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url;
    private final String serviceName;
    private final boolean isGateway;

    private ConcurrentMap<String, String> customHeaders;

    public HertsHttpClientHandler(String url, Class<?> hertsRpcService, boolean isGateway) {
        this.url = url;
        this.serviceName = hertsRpcService.getSimpleName();
        this.isGateway = isGateway;
        init(hertsRpcService.getName());
    }

    public HertsHttpClientHandler(String url, Class<?> hertsRpcService, Map<String, String> customHeaders, boolean isGateway) {
        this.url = url;
        this.serviceName = hertsRpcService.getSimpleName();
        this.isGateway = isGateway;
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
            List<Class<?>> paramTypes = new ArrayList<>(Arrays.asList(method.getParameterTypes()));
            this.methodParameters.put(method.getName(), paramTypes);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String pref = !this.isGateway ? "api" : "gateway";
        HttpRequest.Builder builder = HttpRequest
                .newBuilder(URI.create(url + "/" + pref + "/" + this.serviceName + "/" + method.getName()))
                .header("Content-Type", "application/json");

        if (this.customHeaders != null) {
            for (Map.Entry<String, String> entry : this.customHeaders.entrySet()) {
                builder = builder.header(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest httpRequest;
        InternalHttpRequest body = new InternalHttpRequest();
        if (args != null) {
            List<Class<?>> parameterTypes = this.methodParameters.get(method.getName());
            if (parameterTypes == null || args.length != parameterTypes.size()) {
                throw new InvalidMessageException("Invalid herts method.");
            }

            List<InternalHttpMsg> payloads = new ArrayList<>();
            IntStream.range(0, args.length).forEach(idx -> {
                Object requestArg = args[idx];
                Class<?> aClass = parameterTypes.get(idx);
                InternalHttpMsg payload = new InternalHttpMsg();
                payload.setKeyName("arg" + idx);
                payload.setValue(requestArg);
                payload.setClassInfo(aClass.getName());
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
        try {
            Class<?> aClass = Class.forName(payload.getClassInfo());
            return serializer.convertFromHertHttpPayload(payload.getValue(), aClass);
        } catch (ClassNotFoundException ex) {
            return payload.getValue();
        }
    }

    private void throwHertsHttpError(HttpResponse<byte[]> httpResponse) throws IOException {
        InternalHttpErrorResponse deserialize = this.serializer.deserialize(httpResponse.body(), InternalHttpErrorResponse.class);
        deserialize.throwHertsHttpErrorException();
    }
}
