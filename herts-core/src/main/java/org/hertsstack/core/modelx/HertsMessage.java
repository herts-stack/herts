package org.hertsstack.core.modelx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Herts rpc message of internal
 *
 * @author Herts Contributer
 */
@Message
public class HertsMessage implements Serializable {
    @JsonIgnore
    private Object[] messageParameters;
    @JsonIgnore
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
