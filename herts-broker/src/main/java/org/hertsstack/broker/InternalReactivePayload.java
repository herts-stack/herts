package org.hertsstack.broker;

import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.List;

/**
 * Herts reactive payload for load balancing
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
@Message
public class InternalReactivePayload implements Serializable {
    private String methodName;
    private String clientId;
    private List<Object> parameters;
    private List<Class<?>> parameterTypes;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<Class<?>> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}