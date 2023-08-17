package org.hertsstack.core.modelx;

/**
 * Internal Payload.
 *
 * @author Herts Contributer
 */
public class InternalHttpMsg {
    private String keyName;
    private Object value;

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getKeyName() {
        return keyName;
    }

    public Object getValue() {
        return value;
    }
}


