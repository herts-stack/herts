package org.hertsstack.http;

import java.lang.reflect.Method;

/**
 * Parsed http request info.
 * Parsed uri from definition endpoint. Also, include method info.
 *
 * @author Herts Contributer
 * @version 1.0.2
 */
class ParedHttpRequestInfo {
    private final String serviceName;
    private final Method hertsMethod;

    ParedHttpRequestInfo(String serviceName, Method hertsMethod) {
        this.serviceName = serviceName;
        this.hertsMethod = hertsMethod;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Method getHertsMethod() {
        return hertsMethod;
    }
}
