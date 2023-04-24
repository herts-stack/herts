package org.herts.common.context;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Herts rpc message of internal
 * @author Herts Contributer
 * @version 1.0.0
 */
@Message
public class HertsMsg implements Serializable {
    private Object[] messageParameters;
    private Class<?>[] classTypes;

    public HertsMsg() {
    }

    public HertsMsg(Object[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    public HertsMsg(Class<?>[] classTypes) {
        this.classTypes = classTypes;
    }

    public HertsMsg(Object[] messageParameters, Class<?>[] classTypes) {
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
