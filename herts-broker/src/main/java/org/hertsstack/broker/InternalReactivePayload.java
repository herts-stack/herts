package org.hertsstack.broker;

import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.List;

/**
 * Herts reactive payload for load balancing
 *
 * @author Herts Contributer
 */
@Message
public class InternalReactivePayload implements Serializable {
    private String methodName;
    private String clientId;
    private List<Object> parameters;
    private List<Class<?>> parameterTypes;

    /**
     * Getter.
     *
     * @return ClientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Setter.
     *
     * @param clientId ClientId
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Getter.
     *
     * @return MethodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Setter.
     *
     * @param methodName MethodName
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Getter.
     *
     * @return Parameters
     */
    public List<Object> getParameters() {
        return parameters;
    }

    /**
     * Setter.
     *
     * @param parameters Parameters
     */
    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Getter.
     *
     * @return parameter types
     */
    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    /**
     * Setter.
     *
     * @param parameterTypes Parameter types
     */
    public void setParameterTypes(List<Class<?>> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
