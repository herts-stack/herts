package org.hertsstack.core.modelx;

import java.io.Serializable;

/**
 * Herts rpc message of internal
 *
 * @author Herts Contributer
 */
public class InternalRpcMsg implements Serializable {
    private Object[] messageParameters;
    private Class<?>[] classTypes;

    public InternalRpcMsg() {
    }

    public InternalRpcMsg(Object[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    public InternalRpcMsg(Class<?>[] classTypes) {
        this.classTypes = classTypes;
    }

    public InternalRpcMsg(Object[] messageParameters, Class<?>[] classTypes) {
        this.messageParameters = messageParameters;
        this.classTypes = classTypes;
    }

    public Object[] getMessageParameters() {
        return messageParameters;
    }

    public void setMessageParameters(Object[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    public Class<?>[] getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(Class<?>[] classTypes) {
        this.classTypes = classTypes;
    }
}
