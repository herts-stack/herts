package org.herts.httpclient.handler;

import org.herts.common.context.HertsHttpRequest;
import org.herts.common.exception.HertsMessageException;
import org.herts.common.exception.HertsServiceNotFoundException;
import org.herts.common.serializer.HertsSerializeType;
import org.herts.common.serializer.HertsSerializer;
import org.herts.common.service.HertsService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Herts http client handler
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpClientHandler implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer(HertsSerializeType.Json);
    private final ConcurrentMap<String, List<String>> methodTypes = new ConcurrentHashMap<>();
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
            var paramNames = Arrays.stream(method.getParameters()).map(Parameter::getName).toList();
            methodTypes.put(method.getName(), paramNames);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var parameterNames = methodTypes.get(method.getName());
        if (parameterNames == null || args.length != parameterNames.size()) {
            throw new HertsMessageException("Invalid herts method.");
        }

        var data = new HashMap<String, Object>();
        int index = 0;
        for (Object arg : args) {
            data.put(parameterNames.get(index), arg);
            index++;
        }

        var hertRequest = new HertsHttpRequest();
        hertRequest.setData(data);

        HttpRequest.Builder builder = HttpRequest
                .newBuilder(URI.create(url + "/api/" + this.serviceName + "/" + method.getName()))
                .header("Content-Type", "application/json");

        if (this.customHeaders != null) {
            for (var entry : this.customHeaders.entrySet()) {
                builder = builder.header(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest httpRequest = builder
                .POST(HttpRequest.BodyPublishers.ofString(this.serializer.serializeAsStr(hertRequest)))
                .build();

        HttpResponse<byte[]> httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        return this.serializer.deserialize(httpResponse.body(), method.getReturnType());
    }
}
