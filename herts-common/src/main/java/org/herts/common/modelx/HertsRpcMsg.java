package org.herts.common.modelx;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Herts rpc message of internal
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
@Message
public class HertsRpcMsg implements Serializable {
    private Object[] messageParameters;
    private Class<?>[] classTypes;

    public HertsRpcMsg() {
    }

    public HertsRpcMsg(Object[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    public HertsRpcMsg(Class<?>[] classTypes) {
        this.classTypes = classTypes;
    }

    public HertsRpcMsg(Object[] messageParameters, Class<?>[] classTypes) {
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
