package org.hertsstack.core.modelx;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Herts rpc message of internal
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
@Message
public class HertsMessage implements Serializable {
    private Object[] messageParameters;
    private Class<?>[] classTypes;

    public HertsMessage() {
    }

    public HertsMessage(Object[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    public HertsMessage(Class<?>[] classTypes) {
        this.classTypes = classTypes;
    }

    public HertsMessage(Object[] messageParameters, Class<?>[] classTypes) {
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