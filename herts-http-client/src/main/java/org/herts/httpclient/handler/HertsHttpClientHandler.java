package org.herts.httpclient.handler;

import org.herts.common.modelx.HertsHttpErrorResponse;
import org.herts.common.modelx.HertsHttpRequest;
import org.herts.common.modelx.HertsHttpResponse;
import org.herts.common.modelx.HertsHttpMsg;
import org.herts.common.exception.HertsMessageException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.common.service.HertsService;

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
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpClientHandler implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer(HertsSerializeType.Json);
    private final ConcurrentMap<String, List<Class<?>>> methodParameters = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url;
    private final String serviceName;

    private ConcurrentMap<String, String> customHeaders;

    public HertsHttpClientHandler(String url, Class<?> hertsRpcService) {
        this.url = url;
        this.serviceName = hertsRpcService.getSimpleName();
        init(hertsRpcService.getName());
    }

    public HertsHttpClientHandler(String url, Class<?> hertsRpcService, Map<String, String> customHeaders) {
        this.url = url;
        this.serviceName = hertsRpcService.getSimpleName();
        this.customHeaders = new ConcurrentHashMap<>();
        this.customHeaders.putAll(customHeaders);
        init(hertsRpcService.getName());
    }

    private void init(String instanceName) {
        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(instanceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + HertsService.class.getName());
        }

        Method[] methods = hertsServiceClass.getDeclaredMethods();
        for (Method method : methods) {
            var paramTypes = new ArrayList<>(Arrays.asList(method.getParameterTypes()));
            this.methodParameters.put(method.getName(), paramTypes);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpRequest.Builder builder = HttpRequest
                .newBuilder(URI.create(url + "/api/" + this.serviceName + "/" + method.getName()))
                .header("Content-Type", "application/json");

        if (this.customHeaders != null) {
            for (var entry : this.customHeaders.entrySet()) {
                builder = builder.header(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest httpRequest;
        var body = new HertsHttpRequest();
        if (args != null) {
            var parameterTypes = this.methodParameters.get(method.getName());
            if (parameterTypes == null || args.length != parameterTypes.size()) {
                throw new HertsMessageException("Invalid herts method.");
            }

            var payloads = new ArrayList<HertsHttpMsg>();
            IntStream.range(0, args.length).forEach(idx -> {
                Object requestArg = args[idx];
                Class<?> aClass = parameterTypes.get(idx);
                var payload = new HertsHttpMsg();
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

        HertsHttpResponse deserialize = this.serializer.deserialize(httpResponse.body(), HertsHttpResponse.class);
        if (deserialize == null) {
            return null;
        }

        var payload = deserialize.getPayload();
        try {
            Class<?> aClass = Class.forName(payload.getClassInfo());
            return serializer.convertFromHertHttpPayload(payload.getValue(), aClass);
        } catch (ClassNotFoundException ex) {
            return payload.getValue();
        }
    }

    private void throwHertsHttpError(HttpResponse<byte[]> httpResponse) throws IOException {
        HertsHttpErrorResponse deserialize = this.serializer.deserialize(httpResponse.body(), HertsHttpErrorResponse.class);
        deserialize.throwHertsHttpErrorException();
    }
}
