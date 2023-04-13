package com.tomoyane.herts.httpclient.handler;

import com.tomoyane.herts.hertscommon.context.HertsHttpRequest;
import com.tomoyane.herts.hertscommon.exception.HertsMessageException;
import com.tomoyane.herts.hertscommon.exception.HertsServiceNotFoundException;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializeType;
import com.tomoyane.herts.hertscommon.serializer.HertsSerializer;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

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

/**
 * Herts http client handler
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsHttpClientHandler implements InvocationHandler {
    private final HertsSerializer serializer = new HertsSerializer(HertsSerializeType.Json);
    private final Map<String, List<String>> methodTypes = new HashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url;
    private final String serviceName;

    public HertsHttpClientHandler(String url, HertsCoreService hertsCoreService) {
        this.url = url;
        this.serviceName = hertsCoreService.getClass().getInterfaces()[0].getSimpleName();

        Class<?> hertsServiceClass;
        try {
            hertsServiceClass = Class.forName(hertsCoreService.getClass().getName());
        } catch (ClassNotFoundException ignore) {
            throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + HertsCoreService.class.getName());
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
        var hertRequest = new HertsHttpRequest(data);
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url + "/api/" + this.serviceName + "/" + method.getName()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(this.serializer.serializeAsStr(hertRequest)))
                .build();

        HttpResponse<byte[]> httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        return this.serializer.deserialize(httpResponse.body(), method.getReturnType());
    }
}
